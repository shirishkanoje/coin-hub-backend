package com.shirish.repository;

import com.shirish.modal.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    List<Asset> findByUserId(Long userId );

//    Asset findAssetByIdAndCoinId(Long assetId ,String coinId);
    // ✔️ Correct Query
    Asset findByUserIdAndCoinId(Long userId, String coinId);

}
