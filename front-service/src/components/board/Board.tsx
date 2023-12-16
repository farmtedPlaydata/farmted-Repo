import React, { useRef, useState } from "react";
import BoardBody from "./BoardBody";
import BoardHead from "./boardHead/BoardHead";
import BoardList from "./boardList/BoardList";

// const [globalDTO, setGlobalDTO] = useState<GlobalResponseDTO>();
// 최초 랜더링 시 1페이지 고정
// 게시글 카테고리
enum BoardType{
  AUCTION = "경매",
  COMMISTION = "구매요청",
  SALE = "판매",
  NOTICE = "공지사항",
  CUSTOMER_SERVICE = "고객센터",
  PRODUCT = "상품"
}

// const [pageNo, setPageNo] = useState(1);
// useEffect(() =>{
    // const BOARD_API_ENDPOINT = `https://localhost:8000/boards?page=${pageNo}&category=${category}`;
    // console.log("통신 시작");
    // fetch(BOARD_API_ENDPOINT)  
    //     .then(response => response.json())
    //     .then(json => console.log(json))
    // console.dir(boardList);
// }, [pageNo, category]);

// 요청받는 값
interface GlobalResponseDTO {
    pageInfo:PageInfo;
    boardList:Board[];
    productList?:Product[];
    AuctionList?:Auction[];
}
// 페이지 정보
interface PageInfo {
    pageNo: number;
    totalPage: number;
    // 나중에 board-service에 추가하기
    totalElements: number;
}

// 게시글 정보
interface Board {
    memberName: string;
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
//////////////////////////////////////////////////////////////////////////////
// 더미데이터
const dummyData1: GlobalResponseDTO = {
    pageInfo: { pageNo: 1, totalPage: 5, totalElements: 4 },
    boardList: [
      {
        memberName: "John Doe1",
        boardUuid: "1",
        boardType: BoardType.AUCTION,
        boardTitle: "경매1",
        viewCount: 70,
        createAt: "2023-01-01T12:34:56",
      },
      {
        memberName: "John Doe2",
        boardUuid: "2",
        boardType: BoardType.AUCTION,
        boardTitle: "경매2",
        viewCount: 100,
        createAt: "2023-01-01T12:34:56",
      },
      {
        memberName: "John Doe3",
        boardUuid: "3",
        boardType: BoardType.SALE,
        boardTitle: "경매3",
        viewCount: 50,
        createAt: "2023-01-01T12:34:56",
      }
    ],
    productList: [
      {
        productName: "Product 1",
        productSource: "Source 1",
        productImage: "image1.jpg",
        productPrice: 50
      },
      {
        productName: "Product 2",
        productSource: "Source 2",
        productImage: "image2.jpg",
        productPrice: 50
      },
      {
        productName: "Product 3",
        productSource: "Source 3",
        productImage: "image3.jpg",
        productPrice: 50
      }
    ],
    AuctionList: [
      {
        auctionPrice: 80,
        auctionDeadline: "2023-01-03T16:00:00",
        buyerUUID: "buyer1",
      },
      {
        auctionPrice: 50,
        auctionDeadline: "2024-01-03T16:00:00",
        buyerUUID: "buyer2",
      },
      {
        auctionPrice: 20,
        auctionDeadline: "2023-01-03T16:00:00",
        buyerUUID: "buyer3",
      }
      // Add more auctions as needed
    ],
  };
  // 경매가 아닌 상품 정보만 있는 경우
  const dummyData2: GlobalResponseDTO = {
    pageInfo: { pageNo: 1, totalPage: 5, totalElements: 30 },
    boardList: [
      {
        memberName: 'John Doe',
        boardUuid: '1',
        boardType: BoardType.SALE,
        boardTitle: 'Sale Post',
        viewCount: 100,
        createAt: "2023-01-03T16:00:00",
      },
      {
        memberName: 'John Doe2',
        boardUuid: '2',
        boardType: BoardType.SALE,
        boardTitle: 'Sale Post2',
        viewCount: 7800,
        createAt: "2023-01-03T16:00:00",
      },
    ],
    productList: [
      {
        productName: 'Product 1',
        productSource: 'Source 1',
        productImage: 'image1.jpg',
        productPrice: 50
      },
      {
        productName: 'Product 2',
        productSource: 'Source 2',
        productImage: 'image1.jpg',
        productPrice: 1000
      }
      // Add more products as needed
    ],
    AuctionList: [], // No auctions
  };
  // 게시글에 대한 정보만 있는 경우
  const dummyData3: GlobalResponseDTO = {
    pageInfo: { pageNo: 1, totalPage: 5, totalElements: 20 },
    boardList: [
      {
        memberName: 'John Doe',
        boardUuid: '1',
        boardType: BoardType.CUSTOMER_SERVICE,
        boardTitle: '고객센터',
        viewCount: 50,
        createAt: "2023-01-01T12:34:56",
      },
      // Add more board items as needed
    ],
    productList: [], // No products
    AuctionList: [], // No auctions
  };

//////////////////////////////////////////////////////////////////////////////

  // GlobalResponseDTO까서 board로 변환
  const transformData = (data: GlobalResponseDTO): Board[] => {
    return data.boardList.map((board, index) => {
      const product = data.productList?.[index];
      const auction = data.AuctionList?.[index];
      return {
        ...board,
        productName: product?.productName,
        productSource: product?.productSource,
        productImage: product?.productImage,
        productPrice: product?.productPrice,
        auctionPrice: auction?.auctionPrice,
        auctionDeadline: auction?.auctionDeadline,
      };
    });
  };


const Board = () => {
  let category = useRef(BoardType.PRODUCT);
  
  const dummyData = dummyData1;
  category.current = dummyData.boardList[0].boardType;
  
  return (
      <BoardBody>
          <BoardHead category={category.current} totalBoard={dummyData.pageInfo.totalElements}/>
          <BoardList boardList={transformData(dummyData)}/>
      </BoardBody>
  )
}
export default Board;