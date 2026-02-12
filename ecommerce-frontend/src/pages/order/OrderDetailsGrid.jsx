import dayjs from "dayjs";
import axios from "axios";
import BuyAgainIcon from "../../assets/images/icons/buy-again.png";
import { Fragment } from "react";
import { NavLink } from "react-router";

export function OrderDetailsGrid({ order, loadCart }) {

  return (
    <div className="order-details-grid">
      {order.products.map((productArray) => {
        const addToCart = async () => {
          await axios.post("api/cart-items", {
            productId: productArray.product.id,
            quantity: 1
          });
          await loadCart();
        };

        return (
          <Fragment key={productArray.product.id}>
            <div className="product-image-container">
              <img src={productArray.product.image} />
            </div>

            <div className="product-details">
              <div className="product-name">{productArray.product.name}</div>
              <div className="product-delivery-date">
                Arriving on:{" "}
                {dayjs(productArray.estimatedDeliveryTimeMs).format("MMMM D")}
              </div>
              <div className="product-quantity">
                Quantity: {productArray.quantity}
              </div>
              <button
                className="buy-again-button button-primary"
                onClick={addToCart}
              >
                <img className="buy-again-icon" src={BuyAgainIcon} />
                <span className="buy-again-message">Add to Cart</span>
              </button>
            </div>

            <div className="product-actions">
              <NavLink to={`/tracking/${order.id}/${productArray.product.id}`}>
                <button className="track-package-button button-secondary">
                  Track package
                </button>
              </NavLink>
            </div>
          </Fragment>
        );
      })}
    </div>
  );
}
