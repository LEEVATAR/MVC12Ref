package com.model2.mvc.web.product;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.ProductService;
import com.model2.mvc.service.domain.Product;

@Controller
public class ProductController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;

	@Value("${common.pageUnit}")
	int pageUnit;

	@Value("${common.pageSize}")	
	int pageSize;

	@Value("${common.imagePath}")
	String imagePath;

	@GetMapping("/product/addProduct")
	public String addProductForm(Model model) {
		model.addAttribute("product", new Product());
		return "product/addProduct";
	}

	@PostMapping("/product/addProduct")
	public String addProduct(Product product, @RequestParam("imageName") MultipartFile mFile ) throws Exception {
		String imageName = mFile.getOriginalFilename();
		product.setFileName(imageName);
		productService.addProduct(product);
		
		imageName = System.currentTimeMillis()+"_"+imageName;
		product.setFileName(imageName);
		File saveFile = new File(imagePath, imageName);
		mFile.transferTo(saveFile);
		
		return "redirect:/product/getProduct?prodNo="+product.getProdNo();
	}

	@GetMapping("/product/getProduct")
	public String getProduct( @RequestParam("prodNo") int prodNo , Model model ) throws Exception {
		 Product product = productService.getProduct(prodNo);
		 model.addAttribute("product", product);
		return "product/getProduct";
	}

	@GetMapping("/product/updateProduct")
	public String updateProductForm( @RequestParam("prodNo") int prodNo , Model model ) throws Exception{
		Product product = productService.getProduct(prodNo);
		model.addAttribute("product", product);
		return "product/updateProduct";
	}

	@PostMapping("/product/updateProduct")
	public String updateProduct(Product product, @RequestParam("imageName") MultipartFile mFile) throws Exception{
		String imageName = mFile.getOriginalFilename();
		product.setFileName(imageName);
					
		imageName = System.currentTimeMillis()+"_"+imageName;
		product.setFileName(imageName);
		File saveFile = new File(imagePath, imageName);
		mFile.transferTo(saveFile);
		
		productService.updateProduct(product);
		return "redirect:/product/getProduct?prodNo="+product.getProdNo();
	}

	@GetMapping("/product/listProduct")
	public String listProduct(@ModelAttribute("search") Search search , Model model) throws Exception{
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		Map<String , Object> map=productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		return "product/listProduct";
	}

	@GetMapping("/product/autocomplete")
	public String autocomplete(@RequestParam Map<String, Object> paramMap, Model model) throws Exception{
		List<Map<String, Object>> resultList = productService.autocomplete(paramMap);
		model.addAttribute("resultList", resultList);
		return "product/autocomplete";
	}
}