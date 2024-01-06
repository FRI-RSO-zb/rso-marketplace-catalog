package net.bobnar.marketplace.catalog.api.grpc;

import com.google.protobuf.ByteString;
import com.kumuluz.ee.grpc.annotations.GrpcInterceptor;
import com.kumuluz.ee.grpc.annotations.GrpcService;
import io.grpc.stub.StreamObserver;
import net.bobnar.marketplace.catalog.services.repositories.AdsRepository;
import net.bobnar.marketplace.common.dtos.catalog.v1.ads.Ad;
import net.bobnar.marketplace.common.grpc.catalog.AdsGrpc;
import net.bobnar.marketplace.common.grpc.catalog.AdsService;

import javax.inject.Inject;
import java.util.List;

@GrpcService(interceptors = {
        @GrpcInterceptor(name = "net.bobnar.marketplace.common.grpc.catalog.AdsServiceInterceptor")
})
public class AdsServiceImpl extends AdsGrpc.AdsImplBase {
    @Inject
    private AdsRepository adsRepo;

    @Override
    public void getAds(AdsService.AdsRequest request, StreamObserver<AdsService.AdsResponse> responseObserver) {
        List<Integer> ids = request.getIdsList();
        List<ByteString> identifiers = request.getSourceIdsList().asByteStringList();


//        Ad ad = this.ads.getItem(request.getId());
//        if (ad != null) {
//            AdsService.AdResponse response = AdsService.AdResponse.newBuilder()
//                    .setId(ad.getId())
//                    .setTitle(ad.getTitle())
//                    .build();
//            responseObserver.onNext(response);
//        }

        responseObserver.onCompleted();
    }
}
