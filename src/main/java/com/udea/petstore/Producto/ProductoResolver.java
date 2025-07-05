package com.udea.petstore.Producto;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;

@Controller
public class ProductoResolver {
    private final ProductoRepository productoRepository;

    public ProductoResolver(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @QueryMapping
    public List<Producto> productos() {
        return productoRepository.findAll();
    }

    @QueryMapping
    public Producto producto(@Argument Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("producto no encontrada"));
    }

    @QueryMapping(name = "topProductoMasVendidos")
    public List<Producto> getTopProductoMasVendidos() {
        return productoRepository.findTop5ByOrderByProductoVendidosDesc();
    }

    public record ProductoInput(String nombre, String descripcion, String categoria, Float precio,Long cantidadDisponible,Integer productoVendidos){}

    @MutationMapping(name = "insertarProducto")
    public Producto insertarProducto(@Argument ProductoInput productoInput) {
        Producto producto = new Producto();
        producto.setNombre(productoInput.nombre);
        producto.setDescripcion(productoInput.descripcion);
        producto.setCategoria(productoInput.categoria);
        producto.setProductoVendidos(0);
        producto.setCantidadDisponible(productoInput.cantidadDisponible);
        producto.setprecio(productoInput.precio);
        return productoRepository.save(producto);
    }


    @MutationMapping
    public Boolean deleteProducto(@Argument Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        productoRepository.deleteById(id);
        return true;
    }

    @MutationMapping
    public Producto updateProducto(@Argument Long id, @Argument ProductoInput productoInput) {
        Producto producto = productoRepository.findById(id).orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        int productosVendidosActuales = producto.getProductoVendidos();
        producto.setNombre(productoInput.nombre());
        producto.setDescripcion(productoInput.descripcion());
        producto.setCategoria(productoInput.categoria());
        producto.setProductoVendidos(productosVendidosActuales);
        producto.setprecio(productoInput.precio());
        producto.setCantidadDisponible(productoInput.cantidadDisponible());
        return productoRepository.save(producto);
    }

}
