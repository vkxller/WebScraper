import type { Product } from "../models/product.ts";

export function generateProductCardHtml(
    product: Product
): string {

    const image =
        product.imageUrl === null
            ? `
        <div class="product-image product-image--placeholder">
          📷 Sin imagen disponible
        </div>
      `
            : `
        <div class="product-image">
          <img
            src="${escapeHtml(product.imageUrl)}"
            alt="${escapeHtml(product.name)}"
            loading="lazy"
          />
        </div>
      `;

    const previousPrice =
        product.previousPrice === null
            ? ""
            : `
        <span class="price-previous">
          $${product.previousPrice.toLocaleString("es-CL")}
        </span>
      `;

    const discount =
        product.discount === null
            ? ""
            : `
        <span class="discount-badge">
          ${escapeHtml(product.discount)}
        </span>
      `;

    const sourceUrl =
        product.sourceUrl === null
            ? ""
            : `
        <a
          href="${escapeHtml(product.sourceUrl)}"
          target="_blank"
          rel="noopener noreferrer"
          class="btn-product"
        >
          Ver producto ↗
        </a>
      `;

    return `
    <article class="product-card">
      <div class="card-image-wrapper">
        ${image}
        ${discount}
      </div>

      <div class="card-content">
        <span class="store-badge">
          🟢 ${escapeHtml(product.store)}
        </span>

        <h3 class="product-title" title="${escapeHtml(product.name)}">
          ${escapeHtml(product.name)}
        </h3>

        <div class="price-container">
          <span class="price-current">
            $${product.price.toLocaleString("es-CL")}
          </span>
          ${previousPrice}
        </div>

        ${sourceUrl}
      </div>
    </article>
  `;
}

function escapeHtml(
    value: string
): string {
    return value
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}