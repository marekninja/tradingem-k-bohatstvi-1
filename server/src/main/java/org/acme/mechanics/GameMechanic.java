package org.acme.mechanics;

import org.acme.models.Player;
import org.acme.persistence.ShopPersist;
import org.acme.services.ShopManager;
import redis.clients.jedis.Jedis;

public class GameMechanic implements MechanicsLayer {
    // TODO buy product by PlayerId, ShopId, ProductId (implement Lukas)

    // TODO sell product by PlayerId, ShopId, ProductId (implement Lukas)

    public Boolean checkFinancial(String gameId, Player player, Double moneyToCheck) {
        Jedis jedis = new Jedis("localhost", 6379);
        Double money = jedis.zscore("hra:" + gameId + ":hraci", player.getId() +  ":" + player.getName());
        return money >= moneyToCheck;
    }

    public void buyProduct(Long playerId, Long shopId, Long productId) {
        this.setProductPrice(playerId, shopId, productId, "buy");

    }

    public void sellProduct(Long playerId, Long shopId, Long productId) {
        this.setProductPrice(playerId, shopId, productId, "sell");
    }

    public void setProductPrice(Long playerId, Long shopId, Long productId, String method) {
        Jedis jedis = new Jedis("localhost", 6379);
        String priceCount = jedis.hget("obchod:" + shopId + ":produkty", String.valueOf(productId));

        String[] splitPriceCount = priceCount.split(":");
        int categoryId = Integer.parseInt(splitPriceCount[0]);
        Double price = Double.valueOf(splitPriceCount[1]);
        int count = Integer.parseInt(splitPriceCount[2]);

        if (method == "buy") {
            price -= 0.1 * categoryId;
            count--;
        }
        else if (method == "sell") {
            price += 0.1 * categoryId;
            count++;
        }

        jedis.hset("obchod:" + shopId + ":produkty", String.valueOf(productId), categoryId + ":" + price + ":" + count);
        System.out.println(jedis.hgetAll("obchod:" + shopId + ":produkty"));
    }
}
