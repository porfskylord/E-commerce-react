import { Routes, Route } from 'react-router';
import { HomePage } from './pages/HomePage';
import { CheckoutPage } from './pages/CheckoutPage';
import { OrderPage } from './pages/OrderPage';
import { TrackingPage } from './pages/TrackingPage';

import './App.css'


function App() {
  return(
    <>
      <Routes>
        <Route index element={<HomePage />} />
        <Route path='checkout' element={<CheckoutPage />} />
        <Route path='order' element={<OrderPage />} />
        <Route path='tracking' element={<TrackingPage />} />
      </Routes>
    </>
  );
}

export default App
