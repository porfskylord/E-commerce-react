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

    const fetchAppData = async () => {
      const response = await axios.get('/api/cart-items?expand=product')
      setCart(response.data);
    };

    fetchAppData();
  },[]);

  return (
    <>
      <Routes>
        <Route index element={<HomePage cart={cart} />} />
        <Route path="checkout" element={<CheckoutPage cart={cart} />} />
        <Route path="order" element={<OrderPage cart={cart} />} />
        <Route path="tracking/:orderId/:productId" element={<TrackingPage cart={cart} />} />
        <Route path="*" element={<NotFoundPage cart={cart}/>} />
      </Routes>
    </>
  );
}

export default App;
