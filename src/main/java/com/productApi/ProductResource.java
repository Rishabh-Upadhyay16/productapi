package com.productApi;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Path("/product")
public class ProductResource {


    /**
     *
     */
    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewProduct(Product product) {
        Product.persist(product);
        if (product.isPersistent()) {
            return Response.created(URI.create("/product/" + product.getId())).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    /**
     * This method will return list of product when we hit /product with GET request
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts() {
        List<Product> productsList = Product.listAll();
        return Response.ok(productsList).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getProductById(@PathParam("id") int productId) {
        Optional<Product> optionalProduct = Product.findByIdOptional(productId);
        if (optionalProduct.isPresent()) {
            return Response.ok(optionalProduct.get()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProduct(@PathParam("id") int productId, Product productToUpdate) {
        Optional<Product> optionalProduct = Product.findByIdOptional(productId);
        if (optionalProduct.isPresent()) {
            Product dbProduct = optionalProduct.get();
            if (Objects.nonNull(productToUpdate.getId())) {
                dbProduct.setId(productToUpdate.getId());
            }
            if (Objects.nonNull(productToUpdate.getName())) {
                dbProduct.setName(productToUpdate.getName());
            }
            if (Objects.nonNull(productToUpdate.getPrice())) {
                dbProduct.setPrice(productToUpdate.getPrice());
            }
            if (Objects.nonNull(productToUpdate.getQuantity())) {
                dbProduct.setQuantity(productToUpdate.getQuantity());
            }
            if (Objects.nonNull(productToUpdate.getDescription())) {
                dbProduct.setDescription(productToUpdate.getDescription());
            }
            Product.persist(dbProduct);
            if (productToUpdate.isPersistent()) {
                return Response.created(URI.create("/product/" + dbProduct.getId())).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProduct(@PathParam("id") int productToDeleteId) {
        Optional<Product> optionalProduct = Product.findByIdOptional(productToDeleteId);
        if (optionalProduct.isPresent()) {
            boolean isDeleted = Product.deleteById(productToDeleteId);
            if (isDeleted) {
                return Response.ok().build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

    }


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{id}/stock")
    public Response getStockAvailable(@PathParam("id") int productId, @QueryParam("count") int count) {
        Optional<Product> optionalProduct = Product.findByIdOptional(productId);
        if (optionalProduct.isPresent()) {
            if (optionalProduct.get().getQuantity() >= count) {
                return Response.ok(productId + " is available for " + count + " quantity").build();
            } else {
                return Response.ok(productId + " is not available for " + count + " quantity").build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/SortedProductList")
    public Response getProductInAscendingOrder() {
        List<Product> productsList = Product.listAll();
        List<Product> sortedProductsByPrice = productsList.stream().sorted(Comparator.comparing(Product::getPrice)).collect(Collectors.toList());
        return Response.ok(sortedProductsByPrice).build();
    }

}
