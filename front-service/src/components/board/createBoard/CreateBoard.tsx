import React, { useState, ChangeEvent } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Editor from '../boardHead/boardUtil/EditorComponent';
import BoardTypeSelector from '../boardHead/boardUtil/BoardTypeSelector';
import styled from 'styled-components';
import Swal from 'sweetalert2';

enum BoardType {
  AUCTION = '경매',
  COMMISSION = '구매요청',
  SALE = '판매',
  NOTICE = '공지사항',
  CUSTOMER_SERVICE = '고객센터',
}
const TitleGroup = styled.div`
  display: flex;
  align-items: center; /* 수직 가운데 정렬 (선택 사항) */
`;

const BoardTypeSelectorContainer = styled.div`
  flex: 1; /* 너비 비율 조절 */
  margin-right: 10px; /* 선택적으로 간격 조절 */
`;

const TitleInput = styled.input`
  flex: 4; 
  width: 100%;
  padding: 8px;
  font-size: 16px;
  border: 1px solid #ced4da;
  border-radius: 4px;
  margin-bottom: 10px;
`;

const UtilDiv = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  background-color: #d7ffea;
  padding: 15px;
  border-radius: 8px;
`;

const ImageDiv = styled.div`
  flex: 1;
  max-width: 300px;
`;

const FileInputContainer = styled.div`
  flex: 1;
`;

const PreviewContainer = styled.div`
  margin-left: 20px;
`;

const ButtonContainer = styled.div`
  text-align: right;
  margin-top: 20px;
`;

const SaveButton = styled.button`
  margin-right: 10px;
`;

const ListButton = styled.button`
  float: right;
`;

const ProductInfoContainer = styled.div`
  margin-top: 20px;

  h3 {
    font-size: 1.5rem;
    margin-bottom: 10px;
  }

  table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 10px;

    td {
      padding: 8px;
      border: 1px solid #ddd;
    }

    input {
      width: 100%;
      padding: 8px;
      font-size: 14px;
      border: 1px solid #ddd;
      border-radius: 4px;
      margin-bottom: 5px;
    }
  }
`;

const BoardWrite: React.FC = () => {
  const navigate = useNavigate();
  const onEditorChange = (value: string) => {
    setContent(value);
  };
  const [title, setTitle] = useState<string>('');
  const [content, setContent] = useState<string>('');
  const [file, setFile] = useState<File | null>(null);
  const [imagePreview, setImagePreview] = useState<string | null>(null);
  const [selectedType, setSelectedType] = useState<BoardType | undefined>(undefined);
  const [productInfo, setProductInfo] = useState({
    productName: '',
    productStock: '',
    productPrice: '',
    productSource: '',
  });


  const handleGoBack = () => {
    // 이전 페이지로 이동
    navigate(-1);
  };
  const handleProductChange = (key: string, value: string) => {
    setProductInfo((prev) => ({ ...prev, [key]: value }));
  };
  const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
    const selectedFile = e.target.files?.[0];
    setFile(selectedFile || null);

    // 이미지 미리보기 설정
    if (selectedFile) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setImagePreview(reader.result as string);
      };
      reader.readAsDataURL(selectedFile);
    } else {
      setImagePreview(null);
    }
  };

  const isSupportedFileType = (file: File | null, type: BoardType | undefined): boolean => {
    if (!file) return false;

    const supportedTypes = ['image/png', 'image/jpeg', 'image/gif'];

    // AUCTION 또는 SALE일 때만 허용
    if (type === BoardType.AUCTION || type === BoardType.SALE) {
      return supportedTypes.includes(file.type);
    }

    return false;
  };
  const handleBoardTypeChange = (value: BoardType | undefined) => {
    setSelectedType(value);
  };

  // 저장하기
  const handleSubmit = async () => {
    var myHeaders = new Headers();
  
    const formData = new FormData();
    
    // AUCTION 또는 SALE인 경우
    if (selectedType === BoardType.AUCTION || selectedType === BoardType.SALE) {
      if (!file) {
        console.error('파일을 선택하세요.');
        return;
      }
      if (!isSupportedFileType(file, selectedType)) {
        console.error('지원되지 않는 파일 형식입니다.');
        return;
      }
      // 파일 정보 추가
      formData.append("IMAGE", file);
    }
    
    // 'CREATE' 파트 추가
    const createData = {
      boardType: selectedType,
      boardContent: content,
      boardTitle: title,
      productName: selectedType === BoardType.AUCTION || selectedType === BoardType.SALE ? productInfo.productName : undefined,
      productStock: selectedType === BoardType.AUCTION || selectedType === BoardType.SALE ? productInfo.productStock : undefined,
      productPrice: selectedType === BoardType.AUCTION || selectedType === BoardType.SALE ? productInfo.productPrice : undefined,
      productSource: selectedType === BoardType.AUCTION || selectedType === BoardType.SALE ? productInfo.productSource : undefined,
    };
    const data = new Blob([JSON.stringify(createData)], {type: "application/json"})
    formData.append("CREATE", data);

    const BOARD_API_ENDPOINT = `/api/board-service/boards`;
    fetch(BOARD_API_ENDPOINT, {
        method: 'POST',
        headers: myHeaders,
        body: formData,
      })
      .then(response => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error('게시글 등록 실패');
        }
      })
      .then(result => navigate("/boards/"+result.data))
      .catch( () => {
        console.log('게시글 등록 실패')
        Swal.fire({
          icon: 'error',
          title: '게시글 등록 실패',
          text: '게시글을 등록하는 중 오류가 발생했습니다.',
        });
      });
  };

  return (
    <div>
      <div style={{ padding: '12px' }}>
        <h1>게시글 등록하기</h1>
        <TitleGroup className="form-group">
          <BoardTypeSelectorContainer>
            <BoardTypeSelector category={selectedType} onChange={handleBoardTypeChange} />
          </BoardTypeSelectorContainer>
          <TitleInput
            type="text"
            placeholder="제목"
            className="form-control"
            onChange={(event) => setTitle(event.target.value)}
          />
        </TitleGroup>
        <Editor value={content} onChange={onEditorChange} />
        {selectedType === BoardType.AUCTION || selectedType === BoardType.SALE ? (
          <ProductInfoContainer>
            <h3>제품 정보</h3>
            <table>
              <tbody>
                <tr>
                  <td>상품명</td>
                  <td>
                    <input
                      type="text"
                      placeholder="상품명"
                      onChange={(event) => handleProductChange('productName', event.target.value)}
                    />
                  </td>
                </tr>
                <tr>
                  <td>재고</td>
                  <td>
                    <input
                      type="text"
                      placeholder="재고"
                      onChange={(event) => handleProductChange('productStock', event.target.value)}
                    />
                  </td>
                </tr>
                <tr>
                  <td>가격</td>
                  <td>
                    <input
                      type="text"
                      placeholder="가격"
                      onChange={(event) => handleProductChange('productPrice', event.target.value)}
                    />
                  </td>
                </tr>
                <tr>
                  <td>원산지</td>
                  <td>
                    <input
                      type="text"
                      placeholder="원산지"
                      onChange={(event) => handleProductChange('productSource', event.target.value)}
                    />
                  </td>
                </tr>
              </tbody>
            </table>
          </ProductInfoContainer>
        ) : null}
        <UtilDiv className="form-group">
          <ImageDiv>
            {selectedType === BoardType.AUCTION || selectedType === BoardType.SALE ? (
              <FileInputContainer className="form-group">
                <input type="file" accept=".png, .jpg, .jpeg, .gif" onChange={handleFileChange} />
                {imagePreview && (
                  <PreviewContainer>
                    <p>미리보기:</p>
                    <img src={imagePreview} style={{ maxWidth: '100%', maxHeight: '200px' }} />
                  </PreviewContainer>
                )}
              </FileInputContainer>
            ) : null}
          </ImageDiv>

          <ButtonContainer className="text-center pd12">
            <SaveButton className="lf-button primary" onClick={handleSubmit}>
              저장
            </SaveButton>
            <ListButton onClick={handleGoBack}>뒤로 가기 </ListButton>
          </ButtonContainer>
        </UtilDiv>
      </div>
    </div>
  );
};

export default BoardWrite;