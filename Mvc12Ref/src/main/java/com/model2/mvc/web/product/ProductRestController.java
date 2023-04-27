package com.model2.mvc.web.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.model2.mvc.service.ProductService;
import com.model2.mvc.service.domain.Product;


//==> 회원관리 RestController
@RestController
@RequestMapping("/product/*")
public class ProductRestController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method 구현 않음
		
	public ProductRestController(){
		System.out.println(this.getClass());
	}
	
	@RequestMapping( value="json/getProduct/{prodNo}", method=RequestMethod.GET )
	public Product getProduct( @PathVariable int prodNo ) throws Exception{
		
		System.out.println("/product/json/getProduct : GET");
		
		//Business Logic
		return productService.getProduct(prodNo);
	}

//	@RequestMapping( value="json/login", method=RequestMethod.POST )
//	public Product login(		@RequestBody Product product,
//										HttpSession session ) throws Exception{
//	
//		System.out.println("/user/json/login : POST");
//		//Business Logic
//		System.out.println("::"+product);
//		Product dbProduct=productService.getProduct(product.getProdNo());
//		
////		if( product.getPassword().equals(dbUser.getPassword())){
////			session.setAttribute("user", dbUser);
////		}
//		
//		return dbProduct;
	}
//	@RequestMapping( value="json/login", method=RequestMethod.POST )
//	public Product login(		@RequestBody User user,
//										HttpSession session ) throws Exception{
//	
//		System.out.println("/user/json/login : POST");
//		//Business Logic
//		System.out.println("::"+user);
//		User dbUser=userService.getUser(user.getUserId());
//		
//		if( user.getPassword().equals(dbUser.getPassword())){
//			session.setAttribute("user", dbUser);
//		}
//		
//		return dbUser;
//	}
//}