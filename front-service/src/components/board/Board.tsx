import React, { useEffect, useRef, useState } from "react";
import BoardHead from "./boardHead/BoardHead";
import BoardList from "./boardList/BoardList";
import PageInfomation from "./boardHead/boardUtil/PageInfo";
import { BoardType } from "./boardHead/boardUtil/BoardType";
import { useParams } from "react-router-dom";
// 최초 랜더링 시 1페이지 고정

// 요청받는 값
interface BoardProps {
  writerUuid?: string;
}

interface GlobalResponseDTO {
    page:PageInfo;
    boardList:Board[];
    productList?:Product[];
    AuctionList?:Auction[];
}
// 페이지 정보
interface PageInfo {
    pageNo: number;
    totalPage: number;
    totalElements: number;
}

// 게시글 정보
interface Board {
    memberName: string;
    memberUuid: string;
    boardUuid: string;
    boardType: BoardType;
    boardTitle: string;
    viewCount: number;
    createAt: string;
    productName?: string;
    productSource?: string;
    productImage?: string;
    productPrice?: number;
    auctionPrice?: number;  // 선택적 프로퍼티
    auctionDeadline?: string;  // 선택적 프로퍼티
    buyerUUID?: string;
}
// 상품 정보
interface Product {
    productName: string;
    productSource: string;
    productImage: string;
    productPrice: number;
    auctionPrice?: number;  // 선택적 프로퍼티
    auctionDeadline?: string;  // 선택적 프로퍼티
}
// 경매 정보
interface Auction {
    auctionPrice: number;
    auctionDeadline: string;
    buyerUUID: string;
}

// GlobalResponseDTO까서 board로 변환
const transformData = (data: GlobalResponseDTO | undefined): Board[] => {
  if (!data) { return [];}
  return data.boardList.map((board, index) => {
    const product = data.productList?.[index];
    const auction = data.AuctionList?.[index];
    // 하나라도 값이 있는 경우 해당 값을 auctionDeadline에 저장
    const auctionDeadline = product?.auctionDeadline || auction?.auctionDeadline;

    return {
      ...board,
      productName: product?.productName,
      productSource: product?.productSource,
      productImage: product?.productImage,
      productPrice: product?.productPrice,
      auctionPrice: auction?.auctionPrice,
      auctionDeadline: auctionDeadline,
    };
  });
};

const Board: React.FC<BoardProps> = ({ writerUuid }) => {
  const [category, setCategory] = useState<BoardType>(BoardType.PRODUCT);
  const [page, setPage] = useState(1);
  const [boardList, setBoardList] = useState<GlobalResponseDTO>();
  useEffect(() =>{
    let current_category: string = "";
    switch (category) {
      case BoardType.AUCTION:
        current_category = "AUCTION";
        break;
      case BoardType.COMMISSION:
        current_category = "COMMISSION";
        break;
      case BoardType.SALE:
        current_category = "SALE";
        break;
      case BoardType.NOTICE:
        current_category = "NOTICE";
        break;
      case BoardType.CUSTOMER_SERVICE:
        current_category = "CUSTOMER_SERVICE";
        break;
      case BoardType.PRODUCT:
        current_category = "PRODUCT";
        break;
      default:
        break;
    }
    let BOARD_API_ENDPOINT = `/api/board-service/boards`;
    console.log(current_category);
    if(writerUuid){
      BOARD_API_ENDPOINT += `/seller/${writerUuid}?page=${page}&category=${current_category}`
    } else {
      BOARD_API_ENDPOINT += `?page=${page}&category=${current_category}`;
    } 
    console.log("통신 시작");
    console.log(writerUuid)
    fetch(BOARD_API_ENDPOINT, {method:"GET"})  
        .then(response => {
          if(response.status === 200)
            return response.json()
          else {
            throw Error ('게시글 조회 실패')
          }
        })
        .then(result => setBoardList(result.data))
        .catch((error) => console.log("실패 사유 : "+error))
  }, [page, category]);
  
  // 카테고리 변경 시 호출되는 콜백 함수
  const handleCategoryChange = (newCategory: BoardType) => {
    setCategory(newCategory);
  };

  // 페이지 변경 시 호출되는 콜백 함수
  const handlePageChange = (newPage: number) => {
    setPage(newPage);
  };

  return (
      <>
        <BoardHead category={category} totalBoard={boardList?.page.totalElements} onCategoryChange={handleCategoryChange}/>
        <BoardList boardList={transformData(boardList)}/>
        <PageInfomation pageInfo={boardList?.page} onPageChange={handlePageChange} />
    </>
  )
}
export default Board;