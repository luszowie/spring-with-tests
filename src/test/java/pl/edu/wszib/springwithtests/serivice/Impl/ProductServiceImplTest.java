package pl.edu.wszib.springwithtests.serivice.Impl;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import pl.edu.wszib.springwithtests.dao.ProductDao;
import pl.edu.wszib.springwithtests.dto.ProductDTO;
import pl.edu.wszib.springwithtests.model.Product;
import pl.edu.wszib.springwithtests.model.Vat;
import pl.edu.wszib.springwithtests.service.impl.ProductServiceImpl;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceImplTest {

    @InjectMocks
    ProductServiceImpl productService;

    @Mock
    ProductDao mockDao;

    @Spy
    Mapper mapper = new DozerBeanMapper();

    @Before
    public void setUp(){
        //productService = new ProductServiceImpl();
        initMocks(this);


    }

    @Test
    public void testAdd(){
        //productService.add(null);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("test produkt");
        productDTO.setCost(10d);
        productDTO.setVat(Vat.VALUE_23);

        Product product = Mockito.mock(Product.class);

        Mockito.when(mapper.map(productDTO, Product.class)).thenReturn(product);
        Mockito.when(mockDao.save(product)).thenReturn(product);
        productService.add(productDTO);

        Mockito.verify(mockDao, Mockito.times(1)).save(product);


    }

    @Test
    public void testRemove(){
        int testId = 5;
        productService.remove(testId);

        Mockito.verify(mockDao, Mockito.times(1)).deleteById(testId);

    }

}
