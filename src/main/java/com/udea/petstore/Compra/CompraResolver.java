package com.udea.petstore.Compra;
import com.udea.petstore.Producto.Producto;
import com.udea.petstore.Producto.ProductoRepository;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.Argument;
import java.util.List;

@Controller
public class CompraResolver {

    private final CompraRepository compraRepository;
    private final ProductoRepository productoRepository;

    public CompraResolver(CompraRepository compraRepository, ProductoRepository productoRepository) {
        this.compraRepository = compraRepository;
        this.productoRepository = productoRepository;
    }

    @QueryMapping
    public List<Compra> compras() {
        return compraRepository.findAll();
    }

    @QueryMapping
    public Compra compra(@Argument Long id) {
        return compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
    }

    public record CompraInput(Integer cantidadProductosCompra, Long producto) {}

    @MutationMapping(name = "insertarCompra")
    public Compra insertarCompra(@Argument CompraInput compraInput) {
        Producto producto = productoRepository.findById(compraInput.producto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (compraInput.cantidadProductosCompra() <= 0) {
            throw new RuntimeException("La cantidad de productos comprados debe ser mayor a cero");
        }
        producto.setCantidadDisponible(producto.getCantidadDisponible() + compraInput.cantidadProductosCompra());
        productoRepository.save(producto);
        Compra compra = new Compra();
        compra.setCantidadProductosCompra(compraInput.cantidadProductosCompra());
        compra.setProducto(producto);
        return compraRepository.save(compra);
    }

    @MutationMapping
    public Compra updateCompra(@Argument Long id, @Argument CompraInput compraInput) {Compra compra = compraRepository.findById(id).orElseThrow(() -> new RuntimeException("Compra no encontrada"));
        Producto productoAnterior = compra.getProducto();
        int cantidadAnterior = compra.getCantidadProductosCompra();
        Producto nuevoProducto = productoRepository.findById(compraInput.producto()).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        if (compraInput.cantidadProductosCompra() <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }
        productoAnterior.setCantidadDisponible(productoAnterior.getCantidadDisponible() - cantidadAnterior);
        productoRepository.save(productoAnterior);
        nuevoProducto.setCantidadDisponible(nuevoProducto.getCantidadDisponible() + compraInput.cantidadProductosCompra());
        productoRepository.save(nuevoProducto);
        compra.setProducto(nuevoProducto);
        compra.setCantidadProductosCompra(compraInput.cantidadProductosCompra());
        return compraRepository.save(compra);
    }

    @MutationMapping
    public Boolean deleteCompra(@Argument Long id) {
        Compra compra = compraRepository.findById(id).orElseThrow(() -> new RuntimeException("Compra no encontrada"));
        Producto producto = compra.getProducto();
        int cantidad = compra.getCantidadProductosCompra();
        producto.setCantidadDisponible(producto.getCantidadDisponible() - cantidad);
        productoRepository.save(producto);
        compraRepository.deleteById(id);
        return true;
    }
}
