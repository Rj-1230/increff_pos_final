package com.increff.pos.api;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackOn = ApiException.class)
public class ProductApi {

    @Autowired
    private ProductDao productDao;


    public Integer add(ProductPojo p) throws ApiException {
        ProductPojo a = productDao.getProductPojoFromBarcode(p.getBarcode());
        if(Objects.nonNull(a)){
            throw new ApiException("The product with given barcode already exists");
        }
       return productDao.insert(p);

    }

    public ProductPojo get(Integer id) {
        return productDao.select(id);
    }


    public List<ProductPojo> getAll() {
        return productDao.selectAll();
    }

    public void update(Integer id, ProductPojo p) throws ApiException {
        ProductPojo ex = getCheckProduct(id);
        ex.setName(p.getName());
        ex.setMrp(p.getMrp());
    }

    public  ProductPojo getCheckProduct(Integer id) throws ApiException {
        ProductPojo a = productDao.select(id);
        if(Objects.isNull(a)){
            throw new ApiException("No such product with given id exists !");
        }
        return a;
    }

    public ProductPojo getCheckProduct(String barcode) throws ApiException {
        ProductPojo a = productDao.getProductPojoFromBarcode(barcode);
        if(Objects.isNull(a)){
            throw new ApiException("The product with given barcode doesn't exists");
        }
        return a;
    }
}
