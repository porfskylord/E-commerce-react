import { Routes, Route } from "react-router";
import { useState, useEffect } from "react";
import axios from "axios";
import { HomePage } from "./pages/homepage/HomePage";
import { CheckoutPage } from "./pages/checkout/CheckoutPage";
import { OrderPage } from "./pages/order/OrderPage";
import { TrackingPage } from "./pages/tracking/TrackingPage";
import { NotFoundPage } from "./pages/notfound/NotFoundPage";

import "./App.css";

function App() {
  const [cart, setCart] = useState([]);

  useEffect(() =>{
    axios.get('/api/cart-items?expand=product')
      .then((response) => {
        setCart(response.data)
      });
  },[]);

  return (
    <>
      <Routes>
        <Route index element={<HomePage cart={cart} />} />
        <Route path="checkout" element={<CheckoutPage cart={cart} />} />
        <Route path="order" element={<OrderPage />} />
        <Route path="tracking" element={<TrackingPage />} />
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </>
  );
}

export default App;
