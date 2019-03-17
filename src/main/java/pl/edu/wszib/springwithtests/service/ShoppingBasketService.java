package pl.edu.wszib.springwithtests.service;

import pl.edu.wszib.springwithtests.dto.ProductDTO;
import pl.edu.wszib.springwithtests.dto.ShoppingBasketDTO;
import pl.edu.wszib.springwithtests.model.Product;

public interface ShoppingBasketService extends AbstractService<ShoppingBasketDTO, Integer>{

    ShoppingBasketDTO addProduct(Integer shopingBasketId, ProductDTO productDTO);

}
