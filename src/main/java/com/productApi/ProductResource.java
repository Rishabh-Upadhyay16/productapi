package com.productApi;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.*;
import java.util.stream.Collectors;

@Path("/product")
public class ProductResource {
    List<Product> productsList = new ArrayList<>();

    /**
     * This method will return list of product when we hit /product with GET request
     * @return list of products
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts() {
        return Response.ok(productsList).build();
    }

    /**
     *
     * @param product
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewProduct(Product product) {
        productsList.add(product);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProduct(@PathParam("id") int id,Product productToUpdate) {
        productsList = productsList.stream().map(product1 -> {if(product1.getId()==id){
            return productToUpdate;
        }else{ return product1;}
                }).collect(Collectors.toList());
        return Response.ok(productsList).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProduct(@PathParam("id") int productToDeleteId) {
        Optional<Product> productToDelete = productsList.stream().filter(product -> product.getId()==productToDeleteId).findFirst();
        if(productToDelete.isPresent()) {
            productsList.remove(productToDelete.get());
            return Response.ok(productToDelete).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getProductById(@PathParam("id") int productId) {
        Optional<Product> optionalProduct = productsList.stream().filter(product -> product.getId()==productId).findFirst();
        if(optionalProduct.isPresent()) {
            return Response.ok(optionalProduct.get()).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{id}/stock")
    public Response getStockAvailable(@PathParam("id") int productId,@QueryParam("count") int count) {
        Optional<Product> optionalProduct = productsList.stream().filter(product -> product.getId()==productId).findFirst();
        if(optionalProduct.isPresent()) {
            if (optionalProduct.get().getQuantity() >= count) {
                return Response.ok(productId + " is available for " + count + " quantity").build();
            }
            else {
                return Response.ok(productId + " is not available for " + count + " quantity").build();
            }
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/SortedProductList")
    public Response getProductInAscendingOrder() {
        List<Product> sortedProductsByPrice = productsList.stream().sorted(Comparator.comparing(Product::getPrice)).collect(Collectors.toList());
        return Response.ok(sortedProductsByPrice).build();
    }

}
