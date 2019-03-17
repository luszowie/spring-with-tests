package pl.edu.wszib.springwithtests.serivice.Impl;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.*;
import pl.edu.wszib.springwithtests.NotFoundException;
import pl.edu.wszib.springwithtests.dao.ProductDao;
import pl.edu.wszib.springwithtests.dao.ShoppingBasketDao;
import pl.edu.wszib.springwithtests.dao.ShoppingBasketItemDao;
import pl.edu.wszib.springwithtests.dto.ProductDTO;
import pl.edu.wszib.springwithtests.dto.ShoppingBasketDTO;
import pl.edu.wszib.springwithtests.model.Product;
import pl.edu.wszib.springwithtests.model.ShoppingBasket;
import pl.edu.wszib.springwithtests.model.ShoppingBasketItem;
import pl.edu.wszib.springwithtests.model.Vat;
import pl.edu.wszib.springwithtests.service.ShoppingBasketService;
import pl.edu.wszib.springwithtests.service.impl.ShoppingBasketServiceImpl;

import java.rmi.server.ExportException;
import java.util.Collections;
import java.util.Optional;

@RunWith(JUnit4.class)
public class ShoppingBasketServiceTest {

    @InjectMocks
    ShoppingBasketServiceImpl basketService;

    @Mock
    ProductDao productDao;

    @Mock
    ShoppingBasketDao shoppingBasketDao;

    @Mock
    ShoppingBasketItemDao shoppingBasketItemDao;

    @Spy
    Mapper mapper = new DozerBeanMapper();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testShoppingBasketIdNotExist(){
        int testshoppingBasketId = 67;
        ProductDTO productDTO = Mockito.mock(ProductDTO.class);
        expectedException.expect(NotFoundException.class);
        basketService.addProduct(testshoppingBasketId,productDTO);


    }

    @Test
    public void testShoppinBasketExistProductNotExist(){
        int testshoppingBasketId = 68;
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(343);
        productDTO.setName("Testowy product");
        productDTO.setCost(1212d);
        productDTO.setVat(Vat.VALUE_8);

        ShoppingBasket shoppingBasket = new ShoppingBasket();
        shoppingBasket.setId(testshoppingBasketId);
        Mockito.when(shoppingBasketDao.findById(testshoppingBasketId))
                .thenReturn(Optional.of(shoppingBasket));

        expectedException.expect(NotFoundException.class);
        basketService.addProduct(testshoppingBasketId,productDTO);

    }

    @Test
    public void testSchoppingBasketExistProductExistSchoppingBasketItemNotExist(){
        int testshoppingBasketId = 68;
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(343);
        productDTO.setName("Testowy product");
        productDTO.setCost(1212d);
        productDTO.setVat(Vat.VALUE_8);

        ShoppingBasket shoppingBasket = new ShoppingBasket();
        shoppingBasket.setId(testshoppingBasketId);

        Mockito.when(shoppingBasketDao.findById(testshoppingBasketId))
                .thenReturn(Optional.of(shoppingBasket));

        Mockito.when(productDao.existsById(productDTO.getId())).thenReturn(true);


        ShoppingBasketItem item = new ShoppingBasketItem();
        item.setId(512);
        item.setShoppingBasket(shoppingBasket);
        item.setAmount(3);
        item.setProduct(mapper.map(productDTO, Product.class));

        Mockito.when(shoppingBasketItemDao
                .findByProductIdAndShoppingBasketId(
                        productDTO.getId(), testshoppingBasketId))
                .thenReturn(item);
        Mockito.when(shoppingBasketItemDao
                .findAllByShoppingBasketId(
                        testshoppingBasketId))
                .thenReturn(Collections.singletonList(item));

        ShoppingBasketDTO result = basketService.addProduct(testshoppingBasketId,productDTO);

        Mockito.verify(shoppingBasketItemDao).save(item);

        Assert.assertEquals(testshoppingBasketId, result.getId().intValue());
        Assert.assertEquals(1,result.getItems().size());
        Assert.assertTrue(result.getItems()
                .stream().anyMatch(i -> i.getProduct().getId()
                        .equals(productDTO.getId())));
        Assert.assertTrue(result.getItems()
        .stream().filter(i -> i.getProduct().getId()
                        .equals(productDTO.getId()))
                .findFirst().map(i -> i.getAmount() == 4)
                .orElse(false));

    }

    @Test
    public void testSchoppingBasketExistProductExistSchoppingBasketItemExist(){
        int testshoppingBasketId = 68;
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(343);
        productDTO.setName("Testowy product");
        productDTO.setCost(1212d);
        productDTO.setVat(Vat.VALUE_8);

        ShoppingBasket shoppingBasket = new ShoppingBasket();
        shoppingBasket.setId(testshoppingBasketId);

        Mockito.when(shoppingBasketDao.findById(testshoppingBasketId))
                .thenReturn(Optional.of(shoppingBasket));

        Mockito.when(productDao.existsById(productDTO.getId())).thenReturn(true);


        ShoppingBasketItem item = new ShoppingBasketItem();
        item.setId(512);
        item.setShoppingBasket(shoppingBasket);
        item.setAmount(1);
        item.setProduct(mapper.map(productDTO, Product.class));

       /*Mockito.when(shoppingBasketItemDao
                .findByProductIdAndShoppingBasketId(
                        productDTO.getId(), testshoppingBasketId))
                .thenReturn(item);*/
        Mockito.when(shoppingBasketItemDao
                .findAllByShoppingBasketId(
                        testshoppingBasketId))
                .thenReturn(Collections.singletonList(item));

        ShoppingBasketDTO result = basketService.addProduct(testshoppingBasketId,productDTO);

        /*Mockito.verify(shoppingBasketItemDao).save(item);*/

        Assert.assertEquals(testshoppingBasketId, result.getId().intValue());
        Assert.assertEquals(1,result.getItems().size());
        Assert.assertTrue(result.getItems()
                .stream().anyMatch(i -> i.getProduct().getId()
                        .equals(productDTO.getId())));
        Assert.assertTrue(result.getItems()
                .stream().filter(i -> i.getProduct().getId()
                        .equals(productDTO.getId()))
                .findFirst().map(i -> i.getAmount() == 1)
                .orElse(false));

    }

}
