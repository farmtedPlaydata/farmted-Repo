import './style.css'
import {useNavigate, useParams} from "react-router-dom";
import React, {ChangeEvent, useEffect, useRef, useState} from "react";
import {useCookies} from "react-cookie";
import useUserStore from "../store/user.store";
import MembernameRequestDto from "../dto/request/memberNameRequestDto";



//          component: 유저 페이지          //
export default function User() {

    //          state: 조회하는 유저 이메일 path variable 상태           //
    const { searchEmail } = useParams();
    //          state: 로그인 유저 정보 상태           //
    const { user, setUser } = useUserStore();
    //          state: 본인 여부 상태           //
    const [isMyPage, setMyPage] = useState<boolean>(false);

    //          function: 네비게이트 함수          //
    const navigator = useNavigate();

    //          component: 유저 정보 컴포넌트          //
    const UserInfo = () => {

        //          state: 프로필 이미지 input ref 상태           //
        const fileInputRef = useRef<HTMLInputElement | null>(null);
        //          state: cookie 상태           //
        const [cookies, setCookie] = useCookies();
        //          state: 이메일 상태           //
        const [email, setEmail] = useState<string>('');
        //          state: 프로필 이미지 상태           //
        const [profileImage, setProfileImage] = useState<string | null>('');
        //          state: 기존 닉네임 상태           //
        const [existingmemberName, setExistingmemberName] = useState<string>('');
        //          state: 닉네임 상태           //
        const [memberName, setMemberName] = useState<string>('');
        //          state: 닉네임 변경 상태           //
        const [showChangememberName, setShowChangememberName] = useState<boolean>(false);


        //          event handler: 프로필 이미지 클릭 이벤트 처리          //
        const onProfileImageClickHandler = () => {
            if (!isMyPage) return;
            if (!fileInputRef.current) return;
            fileInputRef.current.click();
        };
        //          event handler: 닉네임 변경 버튼 클릭 이벤트 처리          //
        const onChangememberNameButtonClickHandler = () => {
            if (!showChangememberName) {
                setShowChangememberName(true);
                return;
            }

            const isEqual = memberName === existingmemberName;
            if (isEqual) {
                setShowChangememberName(false);
                return;
            }

            const accessToken = cookies.accessToken;
            if (!accessToken) return;

            const requestBody: MembernameRequestDto = { memberName };

            //// fetch
        }

        //          event handler: 프로필 이미지 변경 이벤트 처리          //
        const onProfileImageChangeHandler = (event: ChangeEvent<HTMLInputElement>) => {
            if (!event.target.files || !event.target.files.length) return;

            const file = event.target.files[0];
            const data = new FormData();
            data.append('file', file);

            /// fetch
        };

        //          event handler: 닉네임 변경 이벤트 처리          //
        const onmemberNameChangeHandler = (event: ChangeEvent<HTMLInputElement>) => {
            const memberName = event.target.value;
            setMemberName(memberName);
        };

        //          effect: 조회하는 유저의 이메일이 변경될 때 마다 실행할 함수          //
        useEffect(() => {
            if (!searchEmail) {
                navigator('/');
                return;
            }
            /// fetch
        }, [searchEmail]);

        //          render: 유저 정보 컴포넌트 렌더링          //
        return (
            <div id='user-info-wrapper'>
            <div className='user-info-container'>
            <div className={isMyPage ? 'user-info-profile-image-box-mypage' : 'user-info-profile-image-box'} onClick={onProfileImageClickHandler}>
        <input ref={fileInputRef} type='file' accept='image/*' style={{ display: 'none' }} onChange={onProfileImageChangeHandler} />
        {profileImage === null ? (
            <div className='user-info-profile-default-image'>
            <div className='user-info-profile-icon-box'>
            <div className='image-box-white-icon'></div>
                </div>
                </div>
        ) : (
            <div className='user-info-profile-image' style={{ backgroundImage: `url(${profileImage})` }}></div>
        )}
        </div>
        <div className='user-info-meta-box'>
        <div className='user-info-memberName-box'>
            {showChangememberName ? (
                    <input className='user-info-memberName-input' type='text' size={memberName.length + 1} value={memberName} onChange={onmemberNameChangeHandler} />
    ) : (
            <div className='user-info-memberName'>{memberName}</div>
        )}
        {isMyPage && (
            <div className='icon-button' onClick={onChangememberNameButtonClickHandler}>
        <div className='edit-light-icon'></div>
            </div>
        )}
        </div>
        <div className='user-info-email'>{email}</div>
            </div>
            </div>
            </div>
    );

    };

    //          render: 유저 페이지 렌더링          //
    return (
        <>
            <UserInfo />
        </>
    )
}