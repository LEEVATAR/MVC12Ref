package com.model2.mvc.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.ProductService;
import com.model2.mvc.service.product.ProductDao;



//==> 상품관리 서비스 구현
@Service("productService")

@Transactional()
public class ProductServiceImpl implements ProductService{
	
	///Field
	@Autowired
	@Qualifier("productDao")
	ProductDao productDao;
	
	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}
	
	///Constructor
	public ProductServiceImpl() {
		System.out.println(this.getClass());
	}

	///Method
	public void addProduct(Product product) throws Exception {
		productDao.addProduct(product);
	}

	public Product getProduct(int prodNo) throws Exception {
		return productDao.getProduct(prodNo);
	}

	public Map<String , Object > getProductList(Search search) throws Exception {
		List<Product> list= productDao.getProductList(search);
		int totalCount = productDao.getTotalCount(search);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list );
		map.put("totalCount", new Integer(totalCount));
		
		return map;
	}
		
	public void updateProduct(Product product) throws Exception {
		productDao.updateProduct(product);
	}
	
	@Override
	public List<Map<String, Object>>autocomplete(Map<String, Object> paramMap) throws Exception{
		return productDao.autocomplete(paramMap);
		}
}