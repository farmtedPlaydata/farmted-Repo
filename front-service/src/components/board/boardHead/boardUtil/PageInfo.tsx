import React from 'react';

interface PageInfo {
    pageNo?: number;
    totalPage?: number;
    totalElements?: number;
  }
  
  interface PaginationProps {
    pageInfo?: PageInfo;
    onPageChange: (pageNumber: number) => void;
  }

const PageInfo: React.FC<PaginationProps> = ({ pageInfo = {}, onPageChange }) => {
    const { pageNo = 0, totalPage = 0 } = pageInfo;
    const pageNumbers: number[] = [];
  
    for (let i = 1; i <= totalPage; i++) {
      pageNumbers.push(i);
    }
  
    return (
      <nav>
        <ul className="pagination justify-content-center">
          {pageNumbers.map((number) => (
            <li key={number} className="page-item">
              <a onClick={() => onPageChange(number)} className="page-link" style={pageNo === number ? { color: '#17a2b8' } : undefined}>
                {number}
              </a>
            </li>
          ))}
        </ul>
      </nav>
    );
  };

export default PageInfo;