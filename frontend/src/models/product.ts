export interface Product {
    store: string;
    name: string;
    price: number;
    previousPrice: number | null;
    discount: string | null;
    sourceUrl: string | null;
    imageUrl: string | null;
}