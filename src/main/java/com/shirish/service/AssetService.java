package com.shirish.service;

import com.shirish.modal.Asset;
import com.shirish.modal.Coin;
import com.shirish.modal.User;

import java.util.List;

public interface AssetService {
    Asset createAsset (User user , Coin coin , double quantity);

    Asset getAssetById(Long  assetId) throws Exception;

    Asset getAssetByUserIdAndId(Long userId,Long assetId);

    List<Asset> getUsersAssets(Long userId);

    Asset updateAsset(Long assetId, double quantity) throws Exception;

    Asset findAssetById(Long assetId);

    Asset findAssetByUserIdAndCoinId(Long userId ,String coinId);

    void deleteAsset(Long assetId);
}
