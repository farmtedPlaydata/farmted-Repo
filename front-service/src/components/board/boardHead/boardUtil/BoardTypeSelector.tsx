import React, { useState } from 'react';
import Select from 'react-select';

interface Props {
  category: BoardType | undefined;
  onChange: (value: BoardType | undefined) => void;
}

enum BoardType {
  AUCTION = '경매',
  COMMISSION = '구매요청',
  SALE = '판매',
  NOTICE = '공지사항',
  CUSTOMER_SERVICE = '고객센터',
}

const MakeSelect: React.FC<Props> = ({ category, onChange }) => {
  const [boardTypeValue, setBoardTypeValue] = useState<{ value: BoardType; label: string } | null>(null);

  // BoardType option 만들기
  const boardTypeOptions = Object.entries(BoardType).map(([key, value]) => {
    return { value, label: value };
  });

  // 커스텀 스타일 지정
  const customStyles = {
    control: (provided: any) => ({
      ...provided,
      backgroundColor: '#d7ffea',
      borderColor: '#215539',
      '&:hover': {
        borderColor: '#215539',
      },
    }),
    option: (provided: any, state: any) => ({
      ...provided,
      backgroundColor: state.isFocused ? '#d7ffea' : 'white',
      color: state.isFocused ? 'black' : '#495057',
    }),
  };

  const handleChange = (selectedOption: any) => {
    setBoardTypeValue(selectedOption);
    onChange(selectedOption?.value);
  };

  return (
    <Select
      onChange={handleChange}
      placeholder="카테고리"
      options={boardTypeOptions}
      value={boardTypeValue}
      styles={customStyles}
    />
  );
};

export default MakeSelect;