package org.acme.services;

import org.acme.mechanics.GameMechanic;
import org.acme.models.Game;
import org.acme.models.Player;
import org.acme.models.Product;
import org.acme.models.Shop;
import org.acme.persistence.PlayerPersist;
import org.acme.persistence.PriceCategoryPersist;
import org.acme.persistence.ProductPersistence;
import org.acme.persistence.ShopPersist;
import redis.clients.jedis.Jedis;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ShopManager {
    private EntityManager em;

    public ShopManager(EntityManager em) {
        this.em = em;
    }

    public void initializeShops(Game game) {
        ShopPersist sp = new ShopPersist(em);
        ProductPersistence pp = new ProductPersistence(em);

        Long gameId = game.getId();
        Float latitudeCenter = game.getLatitude_center();
        Float longitudeCenter = game.getLongitude_center();
        int numShop = game.getMax_shops();
        Float radius = game.getRadius();
        System.out.println(radius);

        Float minLat = latitudeCenter - radius / 100000;
        Float maxLat = latitudeCenter + radius / 100000;
        Float minLon = longitudeCenter - radius / 130000;
        Float maxLon = longitudeCenter + radius / 130000;

        Random r = new Random();
        List<Shop> shops = new ArrayList<>();

        for (int i = 0; i < numShop; i++) {
            Shop shop = new Shop();
            Float latitude = minLat + r.nextFloat() * (maxLat - minLat);
            Float longitude = minLon + r.nextFloat() * (maxLon - minLon);
            System.out.println(latitude);
            System.out.println(longitude);
            String name = "Obchodik " + i;

            shop.setGame(game);
            shop.setName(name);
            shop.setLatitude(latitude);
            shop.setLongitude(longitude);

            sp.create(shop);
            shops.add(shop);
        }
        game.setShops(shops);
    }

    public List getProducts(EntityManager em) {
        ProductPersistence pp = new ProductPersistence(em);
        return pp.getAll();
    }

    public HashMap getShopProducts(String shopId) {
        Jedis jedis = new Jedis("localhost", 6379);
        return (HashMap) jedis.hgetAll("obchod:" + shopId + ":produkty");
    }

    // TODO (implement Lukas or Matej)
    public Shop getShop(String gameId, String shopId) {
        Jedis jedis = new Jedis("localhost", 6379);
        return null;
    }

    // TODO (implement Lukas or Matej)
    public List getAllShop(String gameId) {
        ShopPersist shopPersist = new ShopPersist(em);
        List<Shop> shops = shopPersist.getAllById(Long.parseLong(gameId));
        return shops;
    }

    // TODO implement Set product price in shop by ProductId, ShopId, Price (implement Lukas)

    public void initializeProducts(Game game) {
        ShopPersist shopPersist = new ShopPersist(em);
        List<Shop> shops = shopPersist.getAllById(game);
        List<Object> products = getProducts(em);
        PriceCategoryPersist priceCategoryPersist = new PriceCategoryPersist(em);

        Jedis jedis = new Jedis("localhost", 6379);
        Random rand = new Random();

        for (Shop shop : shops) {
            for (Object product : products) {
                if ((rand.nextInt(100) & 1) == 1) {
                    Product prod = (Product) product;

                    int price = (int) (rand.nextInt((int) ((prod.getPriceCategory().getMax_price() - prod.getPriceCategory().getMin_price()) + 1)) + prod.getPriceCategory().getMin_price());

                    jedis.hset("obchod:" + shop.getId() + ":produkty", String.valueOf(prod.getId()), prod.getPriceCategory().getId() + ":" + price + ":" + "10");
                }
            }
        }
    }

    public String buyProduct(String gameId, String playerId, String shopId, String productId) {
        GameMechanic gameMechanic = new GameMechanic();
        Float price = gameMechanic.buyProduct(gameId, playerId, shopId, productId);

        PlayerPersist playerPersist = new PlayerPersist(em);
        Player player = (Player) playerPersist.get(Long.parseLong(playerId));

        return "success";
    }

    public String sellProduct(String gameId, String playerId, String shopId, String productId) {
        GameMechanic gameMechanic = new GameMechanic();
        Float price = gameMechanic.sellProduct(gameId, playerId, shopId, productId);

        PlayerPersist playerPersist = new PlayerPersist(em);
        Player player = (Player) playerPersist.get(Long.parseLong(playerId));

        return "success";
    }
}
