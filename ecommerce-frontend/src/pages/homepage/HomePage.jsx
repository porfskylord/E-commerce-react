import axios from "axios";
import { useEffect, useState } from "react";
import { Header } from "../../components/Header";
import { ProductsGrid } from "./ProductsGrid";
import "./HomePage.css";

export function HomePage( {cart, loadCart} ) {

  const [products, setProducts] = useState([]);

  useEffect(() => {
    const fetchHomeData = async () => {
      const response = await axios.get('/api/products')
      setProducts(response.data);
    };

    fetchHomeData();
  },[]);

  return (
    <>
      <title>E-Commerce</title>
      <link rel="icon" type="image/svg+xml" href="home-favicon.png" />

      <Header cart={cart} />

      <div className="home-page">
        <ProductsGrid products={products} loadCart={loadCart} />
      </div>
    </>
  );
}
