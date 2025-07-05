package com.udea.petstore;
import com.udea.petstore.Compra.Compra;
import com.udea.petstore.Compra.CompraRepository;
import com.udea.petstore.Producto.Producto;
import com.udea.petstore.Producto.ProductoRepository;
import com.udea.petstore.Venta.Venta;
import com.udea.petstore.Venta.VentaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.graphql.data.method.annotation.QueryMapping;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductoRepository productoRepository;
    private final CompraRepository compraRepository;
    private final VentaRepository ventaRepository;

    public DataLoader(ProductoRepository productoRepository, CompraRepository compraRepository, VentaRepository ventaRepository) {
        this.productoRepository = productoRepository;
        this.compraRepository = compraRepository;
        this.ventaRepository = ventaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
    }


}
