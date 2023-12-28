import React, { ReactEventHandler, useEffect, useState } from 'react';
import { Link, useParams, useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { Container, Button } from 'react-bootstrap';
import ProductComponent from './ProductDetail';
import AuctionComponent from './AuctionDetail';
import Comment from '../../comment/Comment'

interface ProductDetail {
  productName: string;
  productStock: number;
  productPrice: number;
  productSource: string;
  productImage: string;
}

interface AuctionDetail {
  auctionPrice: number;
  auctionBuyer: string;
  auctionDeadline: Date;
  auctionStatus?: boolean;
}

interface BoardDetailDto {
  boardDetail: {
    boardUuid: string;
    memberName: string;
    memberProfile: string;
    boardType: string;
    boardTitle: string;
    boardContent: string;
    viewCount: number;
    createAt: string;
    updateAt: string;
  };
  productDetail?: ProductDetail;
  auctionDetail?: AuctionDetail;
}
const BoardDetailContainer = styled(Container)`
  max-width: 100%;
  margin-top: 32px;
  padding-top: 48px;
  padding-left: 32px;
  padding-right: 32px;
  padding-bottom: 24px;
`;
const DetailHead = styled(Container)`
  border-bottom: 5px solid #1a6900;
  display: flex;
  margin-bottom: 10px;
`;
const TitleH2 = styled.h2`
  display: flex;
  font-size: 24px;
  margin-bottom: 5px;
  color: #000;
`;
const Title = styled.h1`
  font-size: 24px;
  margin-left: 5px;
  margin-bottom: 16px;
  padding-right: 5px;
  color: #000;
`;
const MemberInfo = styled.div`
  display: flex;
  align-items: center;
  margin-left: auto; // 이미지와 memberName을 오른쪽으로 정렬
`;

const Content = styled.div`
  font-size: 16px;
  margin-top: 16px;
  margin-bottom: 32px;
  border-bottom: 1px solid #000;
`;

const InfoContainer = styled.div`
  margin-top: 16px;
  display: flex;
  border-bottom: 1px solid #000;
`;
const ButtonContainer = styled.div`
  text-align: right;
  margin-top: 20px;
`;
const PrimaryButton = styled.button`
  margin-left: 8px;
  padding: 8px 16px;
  font-size: 16px;
`;

const ListButton = styled.button`
  margin-top: 50px;
  float: right;
`;

//////////////////////////////////////////////////////
const BoardDetail = () => {
  const { boardUUID } = useParams<{ boardUUID: string }>();
  const [board, setBoard] = useState<BoardDetailDto | null>(null);
  const [isCurrentUser, setIsCurrentUser] = useState<boolean>(false);
  const navigate = useNavigate();

  useEffect(() => {
    // 백엔드 서버와 통신하여 게시글 상세 정보를 가져오는 로직
    const fetchData = async () => {
      try {
        const BOARD_API_ENDPOINT = `/api/board-service/boards/${boardUUID}`;
        await fetch(BOARD_API_ENDPOINT, {method:"GET"})
          .then(response => response.json())
          .then(result => setBoard(result.data));

        // 로그인된 멤버의 이름과 게시글 작성자의 이름 비교
        setIsCurrentUser(board?.boardDetail?.memberName === '현재 사용자의 이름');
      } catch (error) {
        console.error('게시글 상세 정보를 불러오는 중 에러 발생', error);
      }
    };

    fetchData();
  }, [boardUUID]);
  const handleDelete = async () => {
    try {
      const response = await fetch(`/board-service/boards/${boardUUID}`, { method: 'DELETE' });
      if (response.ok) {
        // 게시글 삭제 후 이동할 페이지로 이동
        navigate('/boards');
      } else {
        console.error('게시글 삭제 실패');
      }
    } catch (error) {
      console.error('게시글 삭제 중 에러 발생', error);
    }
  };
  
  const handleGoBack = () => {
    // 이전 페이지로 이동
    navigate(-1);
  };


  const handleImageError: ReactEventHandler<HTMLImageElement> = (e) => {
    e.currentTarget.src = 'https://framted-product.s3.ap-northeast-2.amazonaws.com/profile.png';
  };

  return (
    <BoardDetailContainer>
      {board && (
      <>
        <DetailHead>
          <TitleH2>
            <Title style={{ borderRight: '2px solid #000' }}>{board.boardDetail.boardType}</Title>
            <Title>{board.boardDetail.boardTitle}</Title>
          </TitleH2>
          <MemberInfo>
            <strong>{board.boardDetail.memberName}</strong>님
            <img
              src={board.boardDetail.memberProfile}
              alt="profile"
              className="img-rounded article-profile-img loading"
              onError={handleImageError}
              data-was-processed="true"
              style={{ height: '42px', width: 'auto', marginRight: '10px' }}
            />
          </MemberInfo>
        </DetailHead>
        <div>
          {/* 게시글 정보 표시 */}
          <InfoContainer>
            <p>수정일: {board.boardDetail.updateAt}</p>
            <p style={{ marginLeft: 'auto' }}>조회수: {board.boardDetail.viewCount}</p>
          </InfoContainer>
          <Content dangerouslySetInnerHTML={{ __html: board.boardDetail.boardContent }} />
          {/* 제품 정보 표시 */}
          {board.productDetail && (
            <ProductComponent productDetail={board.productDetail} />
          )}
          {/* 경매 정보 표시 */}
          {board.auctionDetail && (
            <AuctionComponent auctionDetail={board.auctionDetail} 
            boardUuid={board.boardDetail.boardUuid}/>
          )}
          {/* 현재 사용자가 작성자인 경우에만 삭제 및 수정 버튼을 표시 */}
          {isCurrentUser && (
            <ButtonContainer> 
              <PrimaryButton onClick={handleDelete}>삭제</PrimaryButton>
              <Link to={`/boards/modify/${boardUUID}`}>
                <PrimaryButton>수정</PrimaryButton>
              </Link>
            </ButtonContainer>
          )}
          <ListButton onClick={handleGoBack}>뒤로 가기 </ListButton>
        </div>
        </>
      )}
      <Comment />
    </BoardDetailContainer>
  );
};

export default BoardDetail;