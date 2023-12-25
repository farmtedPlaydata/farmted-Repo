// src/components/LoginPage.tsx
import React from 'react';
import styled from 'styled-components';



const Container = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
`;

const LoginForm = styled.div`
  width: 300px;
  padding: 290px; 
  border: 1px solid #ccc;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  display: flex; 
  flex-direction: column; 
  align-items: center;
  justify-content: center;
`;

const Greetings = styled.h2`
  font-size: 2.5em;
  text-align: center;
  margin-bottom: 10px;
`;

const Description = styled.p`
  text-align: center;
  margin-bottom: 20px;
`;

const Input = styled.input`
  width: 92%;
  padding: 10px;
  margin-bottom: 20px;
  border-radius: 10px;
`;

const Button = styled.button`
  width: 100%;
  padding: 15px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  &:hover {
    background-color: #0052cc; /* 마우스 호버 시 배경색 변경 */
  }

`;
const Button1 = styled.button`
  width: 100%;
  padding: 15px;
  background-color: #fff;
  color: #000;
  border: 1px solid #000;
  border-radius: 20px;
  cursor: pointer;
  margin-bottom: 20px;
  position: relative;

  img {
    width: 30px; /* 이미지 크기 조절 */
    margin-right: 10px; /* 이미지와 텍스트 사이 간격 조절 */
    position: absolute;
    left: 10px; /* 이미지를 텍스트 왼쪽에 정렬 */
    top: 50%;
    transform: translateY(-50%);
  }

  &:hover {
    background-color: #f2f2f2;
  }
`;

// 나머지 코드는 동일

const Button2 = styled.button`
  width: 100%;
  padding: 15px;
  background-color: #fff;
  color: #000;
  border: 1px solid #000;
  border-radius: 20px;
  cursor: pointer;
  margin-bottom: 20px;
  position: relative;

  img {
    width: 30px; /* 이미지 크기 조절 */
    margin-right: 10px; /* 이미지와 텍스트 사이 간격 조절 */
    position: absolute;
    left: 10px; /* 이미지를 텍스트 왼쪽에 정렬 */
    top: 50%;
    transform: translateY(-50%);
  }

  &:hover {
    background-color: #f2f2f2;
  }
`;


const SignupButton = styled(Button)`
  background-color: #458130;
  width: 100%;
  padding: 15px;
  color: #fff;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  margin-top: 20px;
  &:hover {
    background-color: #2f522d;
  }
  
`;

const LoginPage: React.FC = () => {
    return (
        <Container>
            <LoginForm>
                <Greetings>안녕하세요</Greetings>
                <Description>팜티드에 로그인하거나 계정을 만드세요</Description>
                <Input type="text" placeholder="아이디" />
                <Input type="password" placeholder="비밀번호" />
                <Button>계속</Button>
                <SignupButton>회원가입</SignupButton>
                <div style={{margin:'20px 0px'}}>──────  또는  ──────</div>
                <Button1><img src="/icons/google-icon.png" alt="Icon" />Google로 계속</Button1>
                <Button2><img src="/icons/kakaotalk-icon.png" alt="Icon" />Kakao Talk로 계속</Button2>
            </LoginForm>
        </Container>
    );
};

export default LoginPage;
