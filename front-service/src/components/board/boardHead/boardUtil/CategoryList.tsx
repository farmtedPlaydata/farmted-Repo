import React from 'react';
import Select from 'react-select';
import { BoardType } from './BoardType';

interface CategoryListProps {
  categories: BoardType;
  onCategoryChange: (newCategory: BoardType) => void;
}

// Define your styles
const controlStyles = (provided: any) => ({
  ...provided,
  backgroundColor: '#d7ffea',
  borderColor: '#215539',
  ':hover': {
    borderColor: '#215539',
  },
});

const optionStyles = (provided: any, state: any) => ({
  ...provided,
  backgroundColor: state.isFocused ? '#d7ffea' : 'white',
  color: state.isFocused ? 'black' : '#495057',
});

const CategoryList: React.FC<CategoryListProps> = (props) => {
  const { categories, onCategoryChange } = props;

  const boardTypeOptions = Object.values(BoardType).map((category) => ({
    value: category,
    label: category,
  }));

  const handleChange = (selectedOption: any) => {
    onCategoryChange(selectedOption?.value);
  };

  return (
    <div style={{ marginTop: '20px' }}>
      <Select
        options={boardTypeOptions}
        value={{ value: categories, label: categories }}
        onChange={handleChange}
        placeholder="Select a category"
        styles={{
          control: controlStyles,
          option: optionStyles,
        }}
      />
    </div>
  );
};

export default CategoryList;
