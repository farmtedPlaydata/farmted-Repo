import Accordion from 'react-bootstrap/Accordion';
import React from "react";
import {useNavigate} from "react-router-dom";
import {Button, ButtonGroup} from "react-bootstrap";

const Sidebar = () => {

    const navigator = useNavigate();

    const handleHomeClick = () => {
        navigator('/');
    };
    
    const handleAuthenticationClick = () => {
        navigator('/authentication');
    };

    const handleCreateBoardClick = () => {
        navigator('/boards/write');
    };

    const handleBoardDetailClick = () => {
        navigator(`/boards/firstBoardUUID`);
    };

    return (
        <Accordion defaultActiveKey="0">
            <Accordion.Item eventKey="0">
                <Accordion.Header>메뉴 바</Accordion.Header>

                <Accordion.Body>
                    <ButtonGroup vertical>
                        <Button variant="primary" size="sm" onClick={handleHomeClick} className="mb-2">
                            홈으로 이동
                        </Button>
                        <Button variant="primary" size="sm" onClick={handleCreateBoardClick} className="mb-2">
                            게시판 작성 페이지로 이동
                        </Button>
                        <Button variant="primary" size="sm" onClick={handleBoardDetailClick} className="mb-2">
                            게시판 상세 페이지로 이동
                        </Button>
                        <Button variant="primary" size="sm" onClick={handleAuthenticationClick} className="mb-2">
                            인증 페이지로 이동
                        </Button>
                    </ButtonGroup>
                </Accordion.Body>
            </Accordion.Item>

        </Accordion>
    );
};

export default Sidebar;