import { Link, useLocation } from "react-router-dom";
import styled from "styled-components";
import React from "react";

interface Props {
    // children: JSX.Element;
    menuName: string;
    path: string;
}
interface ContainerProps {
    focus: boolean;
}

// const ContentContainer = styled.div`
//   width: 100%;
//   height: 100%;
// `;

const Container = styled.div<ContainerProps>`
  width: 100%;
  height: 100vh;
  display: flex;
  font-size: 1.5rem;
  &:hover {
    cursor: pointer;
  }

  a {
    text-decoration: none;
    color: ${({ theme }) => theme.textColor};
  }
`;

const NavBar = styled.div`
  width: 300px;
  height: 100%;
  background-color: #d9d9d9;
`;

const Menu = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  li {
    width: 100%;
    height: 50px;
  }
`;

const menuList = [
    { menuName: "Main", path: "/" },
    { menuName: "로그인", path: "/auth" },
    { menuName: "게시물", path: "/boards" },
];

const Sidebar = ({ path }: Props) => {
    const { pathname } = useLocation();
    const focus = pathname === path ? true : false;
    return (
        <Container focus={focus}>
            <NavBar>
                <Menu>
                    {menuList.map((menu) => (
                        <li key={menu.menuName}>
                            <Link to={menu.path}>{menu.menuName}</Link>
                        </li>
                    ))}
                </Menu>
            </NavBar>
            {/*<ContentContainer>{children}</ContentContainer>*/}
        </Container>
    );
};

export default Sidebar;