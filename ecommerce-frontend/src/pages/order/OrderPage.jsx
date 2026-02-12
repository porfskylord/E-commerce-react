import axios from "axios";
import dayjs from "dayjs";
import { useState, useEffect, Fragment } from "react";
import { Header } from "../../components/Header";
import { NavLink } from "react-router";
import { formatMoney } from "../../utils/money";
import BuyAgainIcon from "../../assets/images/icons/buy-again.png";
import "./OrderPage.css";
import { products } from "../../../starting-code/data/products";

export function OrderPage({ cart }) {
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    axios.get("/api/orders?expand=products").then((response) => {
      setOrders(response.data);
    });
  }, []);

  return (
    <>
      <title>Orders</title>
      <link rel="icon" type="image/svg+xml" href="orders-favicon.png" />

      <Header cart={cart} />

      <div className="orders-page">
        <div className="page-title">Your Orders</div>

        <div className="orders-grid">
          {orders.map((order) => {
            return (
              <div key={order.id} className="order-container">
                <div className="order-header">
                  <div className="order-header-left-section">
                    <div className="order-date">
                      <div className="order-header-label">Order Placed:</div>
                      <div>{dayjs(order.orderTimeMs).format("MMMM D")}</div>
                    </div>
                    <div className="order-total">
                      <div className="order-header-label">Total:</div>
                      <div>{formatMoney(order.totalCostCents)}</div>
                    </div>
                  </div>

                  <div className="order-header-right-section">
                    <div className="order-header-label">Order ID:</div>
                    <div>{order.id}</div>
                  </div>
                </div>

                <div className="order-details-grid">
                  {order.products.map((productArray) => {
                    return (
                      <Fragment key={productArray.product.id}>
                        <div className="product-image-container">
                          <img src={productArray.product.image} />
                        </div>

                        <div className="product-details">
                          <div className="product-name">
                            {productArray.product.name}
                          </div>
                          <div className="product-delivery-date">
                            Arriving on: {dayjs(productArray.estimatedDeliveryTimeMs).format("MMMM D")}
                          </div>
                          <div className="product-quantity">Quantity: {productArray.quantity}</div>
                          <button className="buy-again-button button-primary">
                            <img
                              className="buy-again-icon"
                              src={BuyAgainIcon}
                            />
                            <span className="buy-again-message">
                              Add to Cart
                            </span>
                          </button>
                        </div>

                        <div className="product-actions">
                          <NavLink to="/tracking">
                            <button className="track-package-button button-secondary">
                              Track package
                            </button>
                          </NavLink>
                        </div>
                      </Fragment>
                    );
                  })}
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </>
  );
}
