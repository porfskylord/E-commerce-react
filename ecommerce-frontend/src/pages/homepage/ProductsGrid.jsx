import { formatMoney } from "../../utils/money";
import CheckmarkIcon from "../../assets/images/icons/checkmark.png";



export function ProductsGrid( {products} ) {
  return (
    <div className="products-grid">
      {products.map((products) => {
        return (
          <div key={products.id} className="product-container">
            <div className="product-image-container">
              <img className="product-image" src={products.image} />
            </div>

            <div className="product-name limit-text-to-2-lines">
              {products.name}
            </div>

            <div className="product-rating-container">
              <img
                className="product-rating-stars"
                src={`images/ratings/rating-${products.rating.stars * 10}.png`}
              />
              <div className="product-rating-count link-primary">
                {products.rating.count}
              </div>
            </div>

            <div className="product-price">
              {formatMoney(products.priceCents)}
            </div>

            <div className="product-quantity-container">
              <select>
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
                <option value="6">6</option>
                <option value="7">7</option>
                <option value="8">8</option>
                <option value="9">9</option>
                <option value="10">10</option>
              </select>
            </div>

            <div className="product-spacer"></div>

            <div className="added-to-cart">
              <img src={CheckmarkIcon} />
              Added
            </div>

            <button className="add-to-cart-button button-primary">
              Add to Cart
            </button>
          </div>
        );
      })}
    </div>
  );
}
