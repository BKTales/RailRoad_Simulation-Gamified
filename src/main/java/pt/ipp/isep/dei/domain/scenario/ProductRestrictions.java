package pt.ipp.isep.dei.domain.scenario;

import pt.ipp.isep.dei.us004.ScenarioCreationDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductRestrictions implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<String, ProductRestriction> productRestrictions = new HashMap<>();


    public void addProductRestriction(String productName, int timeToProduce, double multiplier) {
        productRestrictions.put(productName, new ProductRestriction(timeToProduce, multiplier));
    }

    public void removeProductRestriction(String productName) {
        productRestrictions.remove(productName);
    }

    public ProductRestriction getProductRestriction(String productName) {
        return productRestrictions.get(productName);
    }

    public List<String> getAllProductsWithRestrictions() {
        return new ArrayList<>(productRestrictions.keySet());
    }


    public List<ScenarioCreationDTO.ProductRestrictionDTO> getProductRestrictionDTOList() {
        List<ScenarioCreationDTO.ProductRestrictionDTO> dtoList = new ArrayList<>();
        for (Map.Entry<String, ProductRestriction> entry : productRestrictions.entrySet()) {
            String productName = entry.getKey();
            ProductRestriction restriction = entry.getValue();
            dtoList.add(new ScenarioCreationDTO.ProductRestrictionDTO(productName, restriction.getTimeToProduce(), restriction.getMultiplier(), false));
        }
        return dtoList;
    }




    public static class ProductRestriction implements Serializable {
        private final int timeToProduce;
        private final double multiplier;

        public ProductRestriction(int timeToProduce, double multiplier) {
            this.timeToProduce = timeToProduce;
            this.multiplier = multiplier;
        }

        public int getTimeToProduce() {
            return timeToProduce;
        }

        public double getMultiplier() {
            return multiplier;
        }
    }
}