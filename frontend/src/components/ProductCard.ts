import type { Product } from "../models/product.ts";

export function generateProductCardHtml(
    product: Product
): string {

    const hasUrl =
        product.sourceUrl !== null && product.sourceUrl.trim().length > 0;

    const cleanUrl =
        hasUrl ? escapeHtml(product.sourceUrl!) : "#";

    const linkAttr =
        hasUrl ? `href="${cleanUrl}" target="_blank" rel="noopener noreferrer"` : "";

    const imageInner =
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

    const imageHtml =
        hasUrl
            ? `<a ${linkAttr} class="card-image-link">${imageInner}</a>`
            : imageInner;

    const titleHtml =
        hasUrl
            ? `<h3 class="product-title"><a ${linkAttr} title="${escapeHtml(product.name)}">${escapeHtml(product.name)}</a></h3>`
            : `<h3 class="product-title" title="${escapeHtml(product.name)}">${escapeHtml(product.name)}</h3>`;

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

    const buttonHtml =
        hasUrl
            ? `
        <a
          ${linkAttr}
          class="btn-product"
        >
          Ver en ${escapeHtml(product.store)} ↗
        </a>
      `
            : "";

    return `
    <article class="product-card ${hasUrl ? "product-card--clickable" : ""}" ${hasUrl ? `data-url="${cleanUrl}"` : ""}>
      <div class="card-image-wrapper">
        ${imageHtml}
        ${discount}
      </div>

      <div class="card-content">
        <span class="store-badge">
          🟢 ${escapeHtml(product.store)}
        </span>

        ${titleHtml}

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