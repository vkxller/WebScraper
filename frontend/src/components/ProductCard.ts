import type { Product } from "../models/product.ts";

export function generateProductCardHtml(
    product: Product
): string {

    const hasUrl =
        product.sourceUrl !== null && product.sourceUrl !== undefined && product.sourceUrl.trim().length > 0;

    const cleanUrl =
        hasUrl ? escapeHtml(product.sourceUrl!.trim()) : "";

    const imageHtml =
        product.imageUrl === null || product.imageUrl.trim().length === 0
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
        product.discount === null || product.discount.trim().length === 0
            ? ""
            : `
        <span class="discount-badge">
          ${escapeHtml(product.discount)}
        </span>
      `;

    const buttonHtml =
        hasUrl
            ? `
        <a
          href="${cleanUrl}"
          target="_blank"
          rel="noopener noreferrer"
          class="btn-product"
        >
          Ver en ${escapeHtml(product.store)} ↗
        </a>
      `
            : `
        <span class="btn-product btn-product--disabled">
          Enlace no disponible
        </span>
      `;

    return `
    <article class="product-card">
      <div class="card-image-wrapper">
        ${imageHtml}
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

        ${buttonHtml}
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