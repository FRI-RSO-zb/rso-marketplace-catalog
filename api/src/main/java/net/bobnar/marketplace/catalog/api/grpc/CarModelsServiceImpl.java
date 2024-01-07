package net.bobnar.marketplace.catalog.api.grpc;

import com.kumuluz.ee.grpc.annotations.GrpcService;
import io.grpc.stub.StreamObserver;
import net.bobnar.marketplace.catalog.services.repositories.CarBrandsRepository;
import net.bobnar.marketplace.catalog.services.repositories.CarModelsRepository;
import net.bobnar.marketplace.common.dtos.catalog.v1.carBrands.CarBrand;
import net.bobnar.marketplace.common.dtos.catalog.v1.carModels.CarModel;
import net.bobnar.marketplace.common.grpc.catalog.CarModelsGrpc;
import net.bobnar.marketplace.common.grpc.catalog.CarModelsService;
import net.bobnar.marketplace.data.entities.CarBrandEntity;
import net.bobnar.marketplace.data.entities.CarModelEntity;

import javax.enterprise.inject.spi.CDI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@GrpcService(/*interceptors = {
        @GrpcInterceptor(name = "net.bobnar.marketplace.common.grpc.catalog.CarModelsServiceInterceptor")
}*/)
public class CarModelsServiceImpl extends CarModelsGrpc.CarModelsImplBase {
    @Override
    public void getBrands(CarModelsService.BrandsRequest request, StreamObserver<CarModelsService.BrandsResponse> responseObserver) {
        CarBrandsRepository brandsRepo = CDI.current().select(CarBrandsRepository.class).get();

        List<Integer> ids = request.getIdsList();
        List<CarBrand> results = brandsRepo.getMultipleItems(ids);

        List<String> missingIdentifiers = new ArrayList<>();
        for (String identifier : request.getIdentifiersList()) {
            if (results.stream().noneMatch(x-> x.getPrimaryIdentifier().equals(identifier))) {
                missingIdentifiers.add(identifier);
            }
        }
        results.addAll(brandsRepo.toDtoList(brandsRepo.findByPrimaryIdentifiers(missingIdentifiers)));

        CarModelsService.BrandsResponse.Builder responseBuilder = CarModelsService.BrandsResponse.newBuilder();
        for (CarBrand item : results) {
            responseBuilder.addItems(responseBuilder.addItemsBuilder()
                    .setId(item.getId())
                    .setName(item.getName())
                    .setPrimaryIdentifier(item.getPrimaryIdentifier())
                    .addAllIdentifiers(Arrays.stream(item.getIdentifiers().split(",")).toList())
                    .build());
        }

        CarModelsService.BrandsResponse response = responseBuilder.build();
        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }

    @Override
    public void getModels(CarModelsService.ModelsRequest request, StreamObserver<CarModelsService.ModelsResponse> responseObserver) {
        CarModelsRepository modelsRepo = CDI.current().select(CarModelsRepository.class).get();

        List<Integer> ids = request.getIdsList();
        List<CarModel> results = modelsRepo.getMultipleItems(ids);

        List<CarModelsService.ModelsByIdentifierRequest> missingIdentifiers = new ArrayList<>();
        for (CarModelsService.ModelsByIdentifierRequest identifier : request.getIdentifiersList()) {
            if (results.stream().noneMatch(x-> x.getPrimaryIdentifier().equals(identifier.getIdentifier()) && x.getBrandId().equals(identifier.getBrandId()))) {
                missingIdentifiers.add(identifier);
            }
        }
        for (var missingIdentifier : missingIdentifiers) {
            results.add(modelsRepo.toDto(modelsRepo.findByPrimaryIdentifier(missingIdentifier.getBrandId(), missingIdentifier.getIdentifier())));
        }

        CarModelsService.ModelsResponse.Builder responseBuilder = CarModelsService.ModelsResponse.newBuilder();
        for (CarModel item : results) {
            responseBuilder.addItems(responseBuilder.addItemsBuilder()
                    .setId(item.getId())
                    .setName(item.getName())
                    .setBrandId(item.getBrandId())
                    .setPrimaryIdentifier(item.getPrimaryIdentifier())
                    .addAllIdentifiers(Arrays.stream(item.getIdentifiers().split(",")).toList())
                    .build());
        }

        CarModelsService.ModelsResponse response = responseBuilder.build();
        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }

    private String convertToValidIdentifier(String content) {
        return content != null ? content
                .toLowerCase()
                .replace(' ', '-')
                .replace('/', '-')
                .replace('ë', 'e')
                .replace('é', 'e')
                .replace("Ã©", "e")
                .replace("Å¡", "s") :
                "";
    }

    @Override
    public void resolveBrand(CarModelsService.BrandResolveRequest request, StreamObserver<CarModelsService.BrandResolveResponse> responseObserver) {
        CarBrandsRepository brandsRepo = CDI.current().select(CarBrandsRepository.class).get();

        CarModelsService.BrandResolveResponse.Builder responseBuilder = CarModelsService.BrandResolveResponse.newBuilder();

        String brandName = convertToValidIdentifier(request.getBrand());
        String title = convertToValidIdentifier(request.getTitle());

        CarBrandEntity resolvedBrand = null;
        List<CarBrandEntity> brands = null;

        if (!brandName.isEmpty()) {
            resolvedBrand = brandsRepo.findByPrimaryIdentifier(brandName);

            if (resolvedBrand == null) {
                brands = brandsRepo.findItemsWithIdentifier(brandName);
                for (CarBrandEntity brand : brands) {
                    String[] identifiers = brand.getIdentifiers().split(",");
                    for (String i : identifiers) {
                        if (i.equals(brandName)) {
                            resolvedBrand = brand;
                            break;
                        }
                    }

                    if (resolvedBrand != null) break;
                }
            }

            if (resolvedBrand == null && brandName.contains("-")) {
                String simplifiedBrandName = brandName.split("-")[0];
                CarBrandEntity brand1 = brandsRepo.findByPrimaryIdentifier(simplifiedBrandName);
                if (brand1 != null) {
                    resolvedBrand = brand1;
                } else {
                    brands = brandsRepo.findItemsWithIdentifier(simplifiedBrandName);
                    for (CarBrandEntity brand : brands) {
                        String[] identifiers = brand.getIdentifiers().split(",");
                        for (String i : identifiers) {
                            if (i.equals(simplifiedBrandName)) {
                                resolvedBrand = brand;
                                break;
                            }
                        }

                        if (resolvedBrand != null) break;
                    }
                }

            }
        }

        if (resolvedBrand == null && !title.isEmpty()) {
            String[] titleParts = title.split("-");

            /*if (titleParts.length > 2) {
                String titleThreePart = Arrays.stream(titleParts).limit(3).collect(Collectors.joining("-"));

                brands = brandsRepo.findByPrimaryIdentifier(titleThreePart);
                if (!brands.isEmpty()) {
                    resolvedBrand = brands.get(0);
                }

                if (resolvedBrand == null) {
                    brands = brandsRepo.findItemsWithIdentifier(titleThreePart);
                    for (CarBrandEntity brand : brands) {
                        String[] identifiers = brand.getIdentifiers().split("-");
                        for (String i : identifiers) {
                            if (i.equals(titleThreePart)) {
                                resolvedBrand = brand;
                                break;
                            }
                        }

                        if (resolvedBrand != null) break;
                    }
                }
            }*/

            if (resolvedBrand == null && titleParts.length > 1) {
                String titleTwoPart = Arrays.stream(titleParts).limit(2).collect(Collectors.joining("-"));

                resolvedBrand = brandsRepo.findByPrimaryIdentifier(titleTwoPart);

                if (resolvedBrand == null) {
                    brands = brandsRepo.findItemsWithIdentifier(titleTwoPart);
                    for (CarBrandEntity brand : brands) {
                        String[] identifiers = brand.getIdentifiers().split(",");
                        for (String i : identifiers) {
                            if (i.equals(titleTwoPart)) {
                                resolvedBrand = brand;
                                break;
                            }
                        }

                        if (resolvedBrand != null) break;
                    }
                }
            }

            if (resolvedBrand == null && titleParts.length > 0) {
                String titleSinglePart = titleParts[0];

                resolvedBrand = brandsRepo.findByPrimaryIdentifier(titleSinglePart);

                if (resolvedBrand == null) {
                    brands = brandsRepo.findItemsWithIdentifier(titleSinglePart);
                    for (CarBrandEntity brand : brands) {
                        String[] identifiers = brand.getIdentifiers().split(",");
                        for (String i : identifiers) {
                            if (i.equals(titleSinglePart)) {
                                resolvedBrand = brand;
                                break;
                            }
                        }

                        if (resolvedBrand != null) break;
                    }
                }
            }
        }

        if (resolvedBrand != null) {
            responseBuilder.setResolvedBrand(responseBuilder.getResolvedBrandBuilder()
                    .setId(resolvedBrand.getId())
                    .setName(resolvedBrand.getName())
                    .setPrimaryIdentifier(resolvedBrand.getPrimaryIdentifier())
                    .addAllIdentifiers(Arrays.stream(resolvedBrand.getIdentifiers().split(",")).toList()));
        }

        CarModelsService.BrandResolveResponse response = responseBuilder.build();
        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }

    @Override
    public void resolveModel(CarModelsService.ModelResolveRequest request, StreamObserver<CarModelsService.ModelResolveResponse> responseObserver) {
        CarBrandsRepository brandsRepo = CDI.current().select(CarBrandsRepository.class).get();
        CarModelsRepository modelsRepo = CDI.current().select(CarModelsRepository.class).get();

        CarModelsService.ModelResolveResponse.Builder responseBuilder = CarModelsService.ModelResolveResponse.newBuilder();

        String brandIdentifier = convertToValidIdentifier(request.getBrandPrimaryIdentifier());
        CarBrandEntity brand = brandsRepo.findByPrimaryIdentifier(brandIdentifier);
        if (brand == null) {
            responseObserver.onError(new RuntimeException("Brand not found"));
            return;
        }

        String modelName = convertToValidIdentifier(request.getModel());
        String title = convertToValidIdentifier(request.getTitle());

        CarModelEntity resolvedModel = null;
        List<CarModelEntity> models = null;

        if (!modelName.isEmpty()) {
            resolvedModel = modelsRepo.findByPrimaryIdentifier(brand.getId(), modelName);

            if (resolvedModel == null) {
                models = modelsRepo.findItemsWithIdentifier(brand.getId(), modelName);
                for (CarModelEntity model : models) {
                    String[] identifiers = model.getIdentifiers().split(",");
                    for (String i : identifiers) {
                        if (i.equals(modelName)) {
                            resolvedModel = model;
                            break;
                        }
                    }

                    if (resolvedModel != null) break;
                }
            }

            if (resolvedModel == null && modelName.contains("-")) {
                String simplifiedModelName = modelName.split("-")[0];
                CarModelEntity model1 = modelsRepo.findByPrimaryIdentifier(brand.getId(), simplifiedModelName);
                if (model1 != null) {
                    resolvedModel = model1;
                } else {
                    models = modelsRepo.findItemsWithIdentifier(brand.getId(), simplifiedModelName);
                    for (CarModelEntity model : models) {
                        String[] identifiers = model.getIdentifiers().split(",");
                        for (String i : identifiers) {
                            if (i.equals(simplifiedModelName)) {
                                resolvedModel = model;
                                break;
                            }
                        }

                        if (resolvedModel != null) break;
                    }
                }

            }
        }

        if (resolvedModel == null && !title.isEmpty()) {
            String originalTitle = title;
            if (title.startsWith(brand.getPrimaryIdentifier())) {
                title = title.substring(brand.getPrimaryIdentifier().length() + 1);
            }
            for (String identifier : brand.getIdentifiers().split(",")) {
                if (title.startsWith(identifier)) {
                    title = title.substring(identifier.length() + 1);
                }
            }

            String[] titleParts = title.split("-");

            if (titleParts.length > 2) {
                String titleThreePart = Arrays.stream(titleParts).limit(3).collect(Collectors.joining("-"));

                resolvedModel = modelsRepo.findByPrimaryIdentifier(brand.getId(), titleThreePart);

                if (resolvedModel == null) {
                    models = modelsRepo.findItemsWithIdentifier(brand.getId(), titleThreePart);
                    for (CarModelEntity model : models) {
                        String[] identifiers = model.getIdentifiers().split(",");
                        for (String i : identifiers) {
                            if (i.equals(titleThreePart)) {
                                resolvedModel = model;
                                break;
                            }
                        }

                        if (resolvedModel != null) break;
                    }
                }
            }

            if (resolvedModel == null && titleParts.length > 1) {
                String titleTwoPart = Arrays.stream(titleParts).limit(2).collect(Collectors.joining("-"));

                resolvedModel = modelsRepo.findByPrimaryIdentifier(brand.getId(), titleTwoPart);

                if (resolvedModel == null) {
                    models = modelsRepo.findItemsWithIdentifier(brand.getId(), titleTwoPart);
                    for (CarModelEntity model : models) {
                        String[] identifiers = model.getIdentifiers().split(",");
                        for (String i : identifiers) {
                            if (i.equals(titleTwoPart)) {
                                resolvedModel = model;
                                break;
                            }
                        }

                        if (resolvedModel != null) break;
                    }
                }
            }

            if (resolvedModel == null && titleParts.length > 0) {
                String titleSinglePart = titleParts[0];

                resolvedModel = modelsRepo.findByPrimaryIdentifier(brand.getId(), titleSinglePart);

                if (resolvedModel == null) {
                    models = modelsRepo.findItemsWithIdentifier(brand.getId(), titleSinglePart);
                    for (CarModelEntity model : models) {
                        String[] identifiers = model.getIdentifiers().split(",");
                        for (String i : identifiers) {
                            if (i.equals(titleSinglePart)) {
                                resolvedModel = model;
                                break;
                            }
                        }

                        if (resolvedModel != null) break;
                    }
                }
            }
        }

        if (resolvedModel != null) {
            responseBuilder.setResolvedModel(responseBuilder.getResolvedModelBuilder()
                    .setId(resolvedModel.getId())
                    .setName(resolvedModel.getName())
                    .setBrandId(resolvedModel.getBrandId())
                    .setPrimaryIdentifier(resolvedModel.getPrimaryIdentifier())
                    .addAllIdentifiers(Arrays.stream(resolvedModel.getIdentifiers().split(",")).toList()));
        }

        CarModelsService.ModelResolveResponse response = responseBuilder.build();
        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }

    @Override
    public void findModelByIdentifiers(CarModelsService.FindModelByIdentifierRequest request, StreamObserver<CarModelsService.FindModelByIdentifierResponse> responseObserver) {
        var response = findModelByIdentifiers(request);

        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }

    @Override
    public void findMultipleModelsByIdentifiers(CarModelsService.FindMultipleModelsByIdentifiersRequest request, StreamObserver<CarModelsService.FindMultipleModelsByIdentifiersResponse> responseObserver) {
        var responseBuilder = CarModelsService.FindMultipleModelsByIdentifiersResponse.newBuilder();

        List<CarModelsService.FindModelByIdentifierResponse> results = new ArrayList<>();
        for (var requestItem : request.getRequestsList()) {
            CarModelsService.FindModelByIdentifierResponse result = findModelByIdentifiers(requestItem);
            if (result.hasCandidate()) {
                results.add(result);
            } else {
                results.add(null);
            }
        }

        responseBuilder.addAllResults(results);

        responseObserver.onNext(responseBuilder.build());

        responseObserver.onCompleted();
    }

    private CarModelsService.FindModelByIdentifierResponse findModelByIdentifiers(CarModelsService.FindModelByIdentifierRequest request) {
        CarBrandsRepository brandsRepo = CDI.current().select(CarBrandsRepository.class).get();
        CarModelsRepository modelsRepo = CDI.current().select(CarModelsRepository.class).get();

        CarModelsService.FindModelByIdentifierResponse.Builder responseBuilder = CarModelsService.FindModelByIdentifierResponse.newBuilder();

        CarBrandEntity foundBrand = brandsRepo.findByPrimaryIdentifier(request.getBrandIdentifier());

        if (foundBrand == null) {
            String brandIdentifier = request.getBrandIdentifier();
            for (CarBrandEntity brand : brandsRepo.find(null)) {
                if (Arrays.asList(brand.getIdentifiers().split(",")).contains(brandIdentifier)) {
                    foundBrand = brand;
                }
            }
        }

        var candidateBuilder = responseBuilder.getCandidateBuilder();
        if (foundBrand != null) {
            candidateBuilder.setBrand(candidateBuilder.getBrandBuilder()
                    .setId(foundBrand.getId())
                    .setPrimaryIdentifier(foundBrand.getPrimaryIdentifier())
                    .addAllIdentifiers(Arrays.stream(foundBrand.getIdentifiers().split(",")).toList()));

            CarModelEntity foundModel = modelsRepo.findByPrimaryIdentifier(foundBrand.getId(), request.getModelIdentifier());
            if (foundModel == null) {
                String modelIdentifier = request.getModelIdentifier();
                for (CarModelEntity model : modelsRepo.find(null)) {
                    String modelIdentifiers = model.getIdentifiers();
                    if (modelIdentifiers != null && Arrays.asList(modelIdentifiers.split(",")).contains(modelIdentifier)) {
                        foundModel = model;
                    }
                }
            }

            if (foundModel != null) {
                candidateBuilder
                        .setId(foundModel.getId())
                        .setPrimaryIdentifier(foundModel.getPrimaryIdentifier())
                        .addAllIdentifiers(Arrays.stream(foundModel.getIdentifiers().split(",")).toList());
            }
        }

        if (foundBrand != null) {
            responseBuilder.setCandidate(candidateBuilder);
        }
        return responseBuilder.build();
    }
}
