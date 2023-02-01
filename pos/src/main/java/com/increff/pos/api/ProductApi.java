package com.increff.pos.api;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class ProductApi {

    @Autowired
    private ProductDao productDao;


    @Transactional(rollbackOn = ApiException.class)
    public Integer add(ProductPojo p) throws ApiException {
        ProductPojo a = productDao.getProductPojoFromBarcode(p.getBarcode());
        if(Objects.nonNull(a)){
            throw new ApiException("The product with given barcode already exists");
        }
       return productDao.insert(p);

    }

    @Transactional
    public void delete(Integer id) {
        productDao.delete(id);
    }

    @Transactional
    public ProductPojo get(Integer id) {
        return productDao.select(id);
    }


    @Transactional
    public List<ProductPojo> getAll() {
        return productDao.selectAll();
    }

    @Transactional(rollbackOn = ApiException.class)
    public void update(Integer id, ProductPojo p) throws ApiException {
        ProductPojo ex = getCheck(id);
        ex.setName(p.getName());
        ex.setMrp(p.getMrp());
    }

    @Transactional(rollbackOn = ApiException.class)
    public  ProductPojo getCheck(Integer id) throws ApiException {
        ProductPojo a = productDao.select(id);
        if(Objects.isNull(a)){
            throw new ApiException("No such product with given id exists !");
        }
        return a;
    }

    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo getProductPojoFromBarcode(String barcode) throws ApiException {
        ProductPojo a = productDao.getProductPojoFromBarcode(barcode);
        if(Objects.isNull(a)){
            throw new ApiException("The product with given barcode doesn't exists");
        }
        return a;
    }
}
