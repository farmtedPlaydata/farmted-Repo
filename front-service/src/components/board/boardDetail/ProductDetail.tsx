import React from "react";
import styled from "styled-components";

interface ProductDetailProps {
  productDetail: ProductDetail;
}

interface ProductDetail {
  productName: string;
  productStock: number;
  productPrice: number;
  productSource: string;
  productImage: string;
}

const ProductDetailContainer = styled.td`
  margin-top: 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-bottom: 1px solid #000;
`;

const ProductDetailTable = styled.table`
  width: 100%;
`;

const ProductDetailRow = styled.tr`
  display: flex;
  justify-content: space-between;
  width: 100%;
`;

const ProductDetailTitle = styled.th`
  font-size: 18px;
  margin-bottom: 8px;
  text-align: left;
`;

const ProductDetailContent = styled.td`
  font-size: 16px;
  margin-bottom: 8px;
  text-align: right;
`;

const ProductImage = styled.img`
  max-width: 100%;
  margin-top: 16px;
`;

const ProductDetailComponent: React.FC<ProductDetailProps> = ({ productDetail }) => (
  <ProductDetailContainer>
    <ProductDetailTable>
      <tbody>
        <ProductDetailRow>
            <ProductDetailTitle>상품명</ProductDetailTitle>
            <ProductDetailContent>{productDetail.productName}</ProductDetailContent>
        </ProductDetailRow>
        <ProductDetailRow>
            <ProductDetailTitle>재고</ProductDetailTitle>
            <ProductDetailContent>{productDetail.productStock}</ProductDetailContent>
        </ProductDetailRow>
        <ProductDetailRow>
            <ProductDetailTitle>가격</ProductDetailTitle>
            <ProductDetailContent>{productDetail.productPrice}</ProductDetailContent>
        </ProductDetailRow>
        <ProductDetailRow>
            <ProductDetailTitle>발송지</ProductDetailTitle>
            <ProductDetailContent>{productDetail.productSource}</ProductDetailContent>
        </ProductDetailRow>
        <ProductDetailRow>
            <ProductDetailTitle>사진</ProductDetailTitle>
            <ProductDetailContent>
                <ProductImage src={productDetail.productImage} alt="Product" />
            </ProductDetailContent>
        </ProductDetailRow>
      </tbody>
    </ProductDetailTable>
  </ProductDetailContainer>
);

export default ProductDetailComponent;

