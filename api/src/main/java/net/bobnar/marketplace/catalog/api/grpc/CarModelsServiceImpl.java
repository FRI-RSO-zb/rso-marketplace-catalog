package net.bobnar.marketplace.catalog.api.grpc;

import com.kumuluz.ee.grpc.annotations.GrpcInterceptor;
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

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@GrpcService(/*interceptors = {
        @GrpcInterceptor(name = "net.bobnar.marketplace.common.grpc.catalog.CarModelsServiceInterceptor")
}*/)
public class CarModelsServiceImpl extends CarModelsGrpc.CarModelsImplBase {

    private CarModelsRepository modelsRepo;
    private CarBrandsRepository brandsRepo;

    @Override
    public void findModelByIdentifiers(CarModelsService.FindModelByIdentifierRequest request, StreamObserver<CarModelsService.FindModelByIdentifierResponse> responseObserver) {
        modelsRepo = CDI.current().select(CarModelsRepository.class).get();
        brandsRepo = CDI.current().select(CarBrandsRepository.class).get();

        var response = findModelByIdentifiers(request);

        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }

    @Override
    public void findMultipleModelsByIdentifiers(CarModelsService.FindMultipleModelsByIdentifiersRequest request, StreamObserver<CarModelsService.FindMultipleModelsByIdentifiersResponse> responseObserver) {
        modelsRepo = CDI.current().select(CarModelsRepository.class).get();
        brandsRepo = CDI.current().select(CarBrandsRepository.class).get();

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
        CarModelsService.FindModelByIdentifierResponse.Builder responseBuilder = CarModelsService.FindModelByIdentifierResponse.newBuilder();

        CarBrandEntity foundBrand = null;
        List<CarBrandEntity> brands = brandsRepo.findByPrimaryIdentifier(request.getBrandIdentifier());
        if (!brands.isEmpty()) {
            foundBrand = brands.get(0);
        } else {
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

            CarModelEntity foundModel = null;
            List<CarModelEntity> models = modelsRepo.findByPrimaryIdentifier(request.getModelIdentifier(), foundBrand.getId());
            if (!models.isEmpty()) {
                foundModel = models.get(1);
            } else {
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
