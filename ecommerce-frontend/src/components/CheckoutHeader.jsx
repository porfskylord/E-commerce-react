import { NavLink } from "react-router";
import CheckoutLockIcon from '../assets/images/icons/checkout-lock-icon.png';
import Logo from '../assets/images/logo.png';
import MobileLogo from '../assets/images/mobile-logo.png';
import './CheckoutHeader.css'

export function CheckoutHeader( {cart} ) {

  let totalQuantity = 0;

  cart.forEach((cartItem) => {
    totalQuantity += cartItem.quantity;
  });


  return (
    <div className="checkout-header">
      <div className="header-content">
        <div className="checkout-header-left-section">
          <NavLink to="/">
            <img className="logo" src={Logo} />
            <img className="mobile-logo" src={MobileLogo} />
          </NavLink>
        </div>

        <div className="checkout-header-middle-section">
          Checkout (
          <NavLink className="return-to-home-link" to="/">
            {totalQuantity} items
          </NavLink>
          )
        </div>

        <div className="checkout-header-right-section">
          <img src={CheckoutLockIcon} />
        </div>
      </div>
    </div>
  );
}
