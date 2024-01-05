//package net.bobnar.marketplace.catalog.api.v1.grpc;
//
//import com.kumuluz.ee.grpc.annotations.GrpcService;
//import io.grpc.stub.StreamObserver;
//import net.bobnar.marketplace.catalog.services.repositories.AdsRepository;
//import net.bobnar.marketplace.common.dtos.catalog.v1.ads.Ad;
//import net.bobnar.marketplace.common.grpc.catalog.AdsGrpc;
//import net.bobnar.marketplace.common.grpc.catalog.AdsService;
//
//import javax.inject.Inject;
//
//@GrpcService(interceptors = {
////        @GrpcInterceptor(name = "net.bobnar.marketplace.api.v1.grpc.AdsServiceInterceptor")
//})
//public class AdsServiceImpl extends AdsGrpc.AdsImplBase {
//    @Inject
//    private AdsRepository ads;
//
//    @Override
//    public void getAd(AdsService.AdRequest request, StreamObserver<AdsService.AdResponse> responseObserver) {
//        Ad ad = this.ads.getAd(request.getId());
//        if (ad != null) {
//            AdsService.AdResponse response = AdsService.AdResponse.newBuilder()
//                    .setId(ad.getId())
//                    .setTitle(ad.getTitle())
//                    .build();
//            responseObserver.onNext(response);
//        }
//
//        responseObserver.onCompleted();
//    }
//}
