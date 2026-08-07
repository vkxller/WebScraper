import type { Product } from "../models/product.ts";

export function generateProductCardHtml(
    product: Product
): string {

    const previousPrice =
        product.previousPrice === null
            ? ""
            : `
        <p>
          Precio anterior:
          $${product.previousPrice.toLocaleString("es-CL")}
        </p>
      `;

    const discount =
        product.discount === null
            ? ""
            : `
        <p>
          Descuento:
          ${product.discount}
        </p>
      `;

    const sourceUrl =
        product.sourceUrl === null
            ? ""
            : `
        <a
          href="${escapeHtml(product.sourceUrl)}"
          target="_blank"
          rel="noopener noreferrer"
        >
          Ver producto
        </a>
      `;

    return `
    <article>
      <h2>
        ${escapeHtml(product.name)}
      </h2>

      <p>
        Tienda:
        ${escapeHtml(product.store)}
      </p>

      <p>
        Precio:
        $${product.price.toLocaleString("es-CL")}
      </p>

      ${previousPrice}

      ${discount}

      ${sourceUrl}
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