import './style.css';
import useUserStore from "../store/user.store";
import {useCookies} from "react-cookie";
import {useRef, useState, KeyboardEvent, ChangeEvent} from "react";
import {useNavigate} from "react-router-dom";
import React from "react";
import {SignInRequestDto, SignUpRequestDto} from "../dto/request";
import Swal from "sweetalert2";
import InputBox from "../../inputbox";
import {Address, useDaumPostcodePopup} from "react-daum-postcode";




//          component: 인증 페이지          //
export default function Authentication() {

    //          state: 로그인 유저 전역 상태          //
    const {user, setUser} = useUserStore();
    //          state: 쿠키 상태          //
    const [cookies, setCookie] = useCookies();
    //          state: 화면 상태          //
    const [view, setView] = useState<'sign-in' | 'sign-up'>('sign-in');
    //          state: UUID 상태          //
    const [uuid, setUuid] = useState<string | null>(null);


    //          function: 네비게이트 함수          //
    const navigator = useNavigate();
    const SignInCard = () => {

        //          state: 비밀번호 입력 요소 참조 상태          //
        const passwordRef = useRef<HTMLInputElement | null>(null);

        //          state: 입력한 이메일 상태          //
        const [email, setEmail] = useState<string>('');
        //          state: 입력한 비밀번호 상태          //
        const [password, setPassword] = useState<string>('');
        //          state: 비밀번호 인풋 타입 상태          //
        const [passwordType, setPasswordType] = useState<'text' | 'password'>('password');
        //          state: 비밀번호 인풋 버튼 아이콘 상태          //
        const [passwordIcon, setPasswordIcon] = useState<'eye-off-icon' | 'eye-on-icon'>('eye-off-icon');
        //          state: 로그인 에러 상태          //
        const [error, setError] = useState<boolean>(false);

        //          event handler: 이메일 인풋 key down 이벤트 처리          //
        const onEmailKeyDownHandler = (event: KeyboardEvent<HTMLInputElement>) => {
            if (event.key !== 'Enter') return;
            if (!passwordRef.current) return;
            passwordRef.current.focus();
        }

        //          event handler: 비밀번호 인풋 key down 이벤트 처리          //
        const onPasswordKeyDownHanlder = (event: KeyboardEvent<HTMLInputElement>) => {
            if (event.key !== 'Enter') return;
            onSignInButtonClickHandler();
        }
        //          event handler: 비밀번호 인풋 버튼 클릭 이벤트 처리          //
        const onPasswordIconClickHandler = () => {
            if (passwordType === 'text') {
                setPasswordType('password');
                setPasswordIcon('eye-off-icon');
            }
            if (passwordType === 'password') {
                setPasswordType('text');
                setPasswordIcon('eye-on-icon');
            }
        }

        //          event handler: 로그인 버튼 클릭 이벤트 처리          //
        const onSignInButtonClickHandler = async () => {
            const requestBody: SignInRequestDto = { email, password };

            const SIGN_IN_ENDPOINT = '/api/pass-service/login';

            const response = await fetch(SIGN_IN_ENDPOINT, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(requestBody),
                }
            ).then(response=>{
                    const uuidFromHeader = response.headers.get("UUID");
                    setUuid(uuidFromHeader);
                    navigator("/boards");
                }
            )
        };

        //          event handler: 회원가입 링크 클릭 이벤트 처리          //
        const onSignUpLinkClickHandler = () => {
            setView('sign-up');
        }

        //          render: sign in 카드 컴포넌트 렌더링         //
        return (
            <div className='auth-card'>
                <div className='auth-card-top'>
                    <div className='auth-card-title-box'>
                        <div className='auth-card-title'>{'로그인'}</div>
                    </div>
                    <InputBox label='이메일 주소' type='text' placeholder='이메일 주소를 입력해주세요.' error={error} value={email} setValue={setEmail} onKeyDown={onEmailKeyDownHandler} />
                    <InputBox ref={passwordRef} label='비밀번호' type={passwordType} placeholder='비밀번호를 입력해주세요.' error={error} value={password} setValue={setPassword} icon={passwordIcon} onKeyDown={onPasswordKeyDownHanlder} onButtonClick={onPasswordIconClickHandler} />
                </div>
                <div className='auth-card-bottom'>
                    {error && (
                        <div className='auth-sign-in-error-box'>
                            <div className='auth-sign-in-error-message'>
                                {'이메일 주소 또는 비밀번호를 잘못 입력했습니다.\n입력하신 내용을 다시 확인해주세요.'}
                            </div>
                        </div>
                    )}
                    <div className='auth-button' onClick={onSignInButtonClickHandler}>{'로그인'}</div>
                    <div className='auth-description-box'>
                        <div className='auth-description'>{'신규 사용자이신가요? '}<span className='description-emphasis' onClick={onSignUpLinkClickHandler}>{'회원가입'}</span></div>
                    </div>
                </div>
            </div>
        );
    }

    //          component: sign up 카드 컴포넌트          //
    const SignUpCard = () => {

        //          state: 페이지 번호 상태          //
        const [page, setPage] = useState<1 | 2>(1);

        //          state: 이메일 상태          //
        const [email, setEmail] = useState<string>('');
        //          state: 이메일 에러 상태          //
        const [emailError, setEmailError] = useState<boolean>(false);
        //          state: 이메일 에러 메세지 상태          //
        const [emailErrorMessage, setEmailErrorMessage] = useState<string>('');

        //          state: 비밀번호 상태          //
        const [password, setPassword] = useState<string>('');
        //          state: 비밀번호 타입 상태          //
        const [passwordType, setPasswordType] = useState<'text' | 'password'>('password');
        //          state: 비밀번호 아이콘 상태          //
        const [passwordIcon, setPasswordIcon] = useState<'eye-on-icon' | 'eye-off-icon'>('eye-off-icon');
        //          state: 비밀번호 에러 상태          //
        const [passwordError, setPasswordError] = useState<boolean>(false);
        //          state: 비밀번호 에러 메세지 상태          //
        const [passwordErrorMessage, setPasswordErrorMessage] = useState<string>('');

        //          state: 비밀번호 확인 상태          //
        const [passwordCheck, setPasswordCheck] = useState<string>('');
        //          state: 비밀번호 확인 타입 상태          //
        const [passwordCheckType, setPasswordCheckType] = useState<'text' | 'password'>('password');
        //          state: 비밀번호 확인 아이콘 상태          //
        const [passwordCheckIcon, setPasswordCheckIcon] = useState<'eye-on-icon' | 'eye-off-icon'>('eye-off-icon');
        //          state: 비밀번호 확인 에러 상태          //
        const [passwordCheckError, setPasswordCheckError] = useState<boolean>(false);
        //          state: 비밀번호 확인 에러 메세지 상태          //
        const [passwordCheckErrorMessage, setPasswordCheckErrorMessage] = useState<string>('');

        //          state: 이름 상태          //
        const [memberName, setmemberName] = useState<string>('');
        //          state: 이름 에러 상태          //
        const [memberNameError, setMemberNameError] = useState<boolean>(false);
        //          state: 이름 에러 메세지 상태          //
        const [memberNameErrorMessage, setMemberNameErrorMessage] = useState<string>('');

        //          state: 전화번호 상태          //
        const [memberPhone, setmemberPhone] = useState<string>('');
        //          state: 전화번호 에러 상태          //
        const [memberPhoneError, setMemberPhoneError] = useState<boolean>(false);
        //          state: 전화번호 에러 메세지 상태          //
        const [telNumbeErrorMessage, setMemberPhoneErrorMessage] = useState<string>('');

        //          state: 주소 상태          //
        const [memberAddress, setMemberAddress] = useState<string>('');
        //          state: 주소 에러 상태          //
        const [memberAddressError, setMemberAddressError] = useState<boolean>(false);
        //          state: 주소 에러 메세지 상태          //
        const [memberAddressErrorMessage, setMemberAddressErrorMessage] = useState<string>('');

        //          state: 상세 주소 상태          //
        const [memberAddressDetail, setMemberAddressDetail] = useState<string>('');

        //          state: 개인정보동의 상태          //
        const [consent, setConsent] = useState<boolean>(false);
        //          state: 개인정보동의 에러 상태          //
        const [consentError, setConsentError] = useState<boolean>(false);

        //          state: 프로필 이미지 상태          //
        const [profileImage, setProfileImage] = useState<File | string>('');


        //          event handler: 프로필 이미지 이벤트 처리          //
        const profileImageChange = (e: ChangeEvent<HTMLInputElement>) => {
            if (e.target.files) {
                setProfileImage(e.target.files[0]);
            } else {
                setProfileImage('https://framted-product.s3.ap-northeast-2.amazonaws.com/profile.png');
            }
        };

        //          function: 다음 주소 검색 팝업 오픈 함수          //
        const open = useDaumPostcodePopup();


        //          event handler: 비밀번호 아이콘 클릭 이벤트 처리          //
        const onPasswordIconClickHandler = () => {
            if (passwordType === 'password') {
                setPasswordType('text');
                setPasswordIcon('eye-on-icon');
            }
            if (passwordType === 'text') {
                setPasswordType('password');
                setPasswordIcon('eye-off-icon');
            }
        }
        //          event handler: 비밀번호 확인 아이콘 클릭 이벤트 처리          //
        const onPasswordCheckIconClickHandler = () => {
            if (passwordCheckType === 'text') {
                setPasswordCheckType('password');
                setPasswordCheckIcon('eye-off-icon');
            }
            if (passwordCheckType === 'password') {
                setPasswordCheckType('text');
                setPasswordCheckIcon('eye-on-icon');
            }
        }
        //          event handler: 주소 아이콘 클릭 이벤트 처리          //
        const onmemberAddressIconClickHandler = () => {
            open({ onComplete });
        }
        //          event handler: 다음 주소 검색 완료 이벤트 처리          //
        const onComplete = (data: Address) => {
            const memberAddress = data.address;
            setMemberAddress(memberAddress);
        }
        //          event handler: 개인정보동의 체크 이벤트 처리          //
        const onConsentCheckHandler = () => {
            setConsent(!consent);
        }

        //          event handler: 회원가입 이벤트 처리          //
        const onSignUpButtonClickHandler = async () => {

            setEmailError(false);
            setEmailErrorMessage('');
            setPasswordError(false);
            setPasswordErrorMessage('');
            setPasswordCheckError(false);
            setPasswordCheckErrorMessage('');

            // description: 이메일 패턴 확인 //
            const emailPattern = /^[a-zA-Z0-9]*@([-.]?[a-zA-Z0-9])*\.[a-zA-Z]{2,4}$/;
            const checkedEmail = !emailPattern.test(email);
            if (checkedEmail) {
                setEmailError(true);
                setEmailErrorMessage('이메일 형식으로 입력해주세요.');
            }
            // description: 비밀번호 길이 확인 //
            const checkedPassword = password.trim().length < 8;
            if (checkedPassword) {
                setPasswordError(true);
                setPasswordErrorMessage('비밀번호는 8자 이상 입력해주세요.');
            }
            // description: 비밀번호 일치 여부 확인 //
            const checkedPasswordCheck = password !== passwordCheck;
            if (checkedPasswordCheck) {
                setPasswordCheckError(true);
                setPasswordCheckErrorMessage('비밀번호가 일치하지않습니다.');
            }

            if (checkedEmail || checkedPassword || checkedPasswordCheck) return;

            const requestBody: SignInRequestDto = {email, password};

            const DETAILS_ENDPOINT = '/api/pass-service/passes';

            fetch(DETAILS_ENDPOINT, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(requestBody),
            }).then(response => {
                if (response.ok) {
                    const uuidFromHeader = response.headers.get("UUID");
                    setUuid(uuidFromHeader);
                    return response.text();
                } else {
                    throw new Error('회원 정보 작성에 실패했습니다.');
                }
            }).then(() => {
                setPage(2);
            })
                .catch(()=>{
                Swal.fire({
                    icon: 'error',
                    title: '이미 존재하는 회원이 있습니다.',
                    text: '회원가입 중 오류가 발생했습니다.'
                })
            });

        }

        //          event handler: 상세 정보 입력 버튼 클릭 이벤트 처리          //
        const onDetailsButtonClickHandler = async () => {

            setMemberNameError(false);
            setMemberNameErrorMessage('');
            setMemberPhoneError(false);
            setMemberPhoneErrorMessage('');
            setMemberAddressError(false);
            setMemberAddressErrorMessage('');
            setConsentError(false);

            // description: 이메일 여부 확인 //
            const checkedEmail = email.trim().length === 0;
            if (checkedEmail) {
                setEmailError(true);
                setEmailErrorMessage('이메일을 확일할 수 없습니다.');
            }

            // description: UUID 여부 확인 //
            const checkedUuid = uuid?.trim().length === 0;
            if (checkedUuid) {
                alert('uuid를 확인할 수 없습니다.');
            }

            // description: 이름 입력 여부 확인 //
            const checkedmemberName = memberName.trim().length === 0;
            if (checkedmemberName) {
                setMemberNameError(true);
                setMemberNameErrorMessage('이름을 입력해주세요.');
            }
            // description: 전화번호 입력 여부 확인 //
            const memberPhonePattern = /^[0-9]{10,12}$/;
            const checkedmemberPhone = !memberPhonePattern.test(memberPhone);
            if (checkedmemberPhone) {
                setMemberPhoneError(true);
                setMemberPhoneErrorMessage('숫자만 입력해주세요.');
            }
            // description: 주소 입력 여부 확인 //
            const checkedmemberAddress = memberAddress.trim().length === 0;
            if (checkedmemberAddress) {
                setMemberAddressError(true);
                setMemberAddressErrorMessage('우편번호를 선택해주세요.');
            }

            // description: 개인정보동의 여부 확인 //
            if (!consent) setConsentError(true);

            var myHeaders = new Headers();

            const formData = new FormData();
            formData.append("IMAGE", profileImage);

            if (checkedmemberName || checkedmemberPhone || checkedmemberAddress || !consent) return;

            const requestBody: SignUpRequestDto = {
                email, uuid,
                memberAddress, memberName,
                memberPhone, memberAddressDetail,
                consent};
            const data = new Blob([JSON.stringify(requestBody)], {type: "application/json"})
            formData.append("CREATE", data);

            const DETAILS_ENDPOINT = '/api/member-service/members';

            fetch(DETAILS_ENDPOINT, {
                method: 'POST',
                headers: myHeaders,
                body: formData,
            }).then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('회원 정보 작성에 실패했습니다.');
                }
            });
            navigator('/');
        }



        //          render: sign up 카드 컴포넌트 렌더링         //
        return (
            <div className='auth-card'>
                <div className='auth-card-top'>
                    <div className='auth-card-title-box'>
                        <div className='auth-card-title'>{'회원가입'}</div>
                        <div className='auth-card-title-page'>{`${page}/2`}</div>
                    </div>
                    {page === 1 && (<>
                        <InputBox label='이메일 주소*' type='text' placeholder='이메일 주소를 입력해주세요.' value={email} setValue={setEmail} error={emailError} errorMessage={emailErrorMessage} />
                        <InputBox label='비밀번호*' type={passwordType} placeholder='비밀번호를 입력해주세요.' value={password} setValue={setPassword} icon={passwordIcon} error={passwordError} errorMessage={passwordErrorMessage} onButtonClick={onPasswordIconClickHandler} />
                        <InputBox label='비밀번호 확인*' type={passwordCheckType} placeholder='비밀번호를 다시 입력해주세요.' value={passwordCheck} setValue={setPasswordCheck} icon={passwordCheckIcon} error={passwordCheckError} errorMessage={passwordCheckErrorMessage} onButtonClick={onPasswordCheckIconClickHandler} />
                        <div className='auth-button' onClick={onSignUpButtonClickHandler}>{'다음으로'}</div>
                    </>)}
                    {page === 2 && (<>
                        <InputBox label='이름*' type='text' placeholder='이름을 입력해주세요.' value={memberName} setValue={setmemberName} error={memberNameError} errorMessage={memberNameErrorMessage} />
                        <InputBox label='전화번호*' type='text' placeholder='전화번호를 입력해주세요.' value={memberPhone} setValue={setmemberPhone} error={memberPhoneError} errorMessage={telNumbeErrorMessage} />
                        <InputBox label='주소*' type='text' placeholder='우편번호 찾기' value={memberAddress} setValue={setMemberAddress} icon='right-arrow-icon' error={memberAddressError} errorMessage={memberAddressErrorMessage} onButtonClick={onmemberAddressIconClickHandler} />
                        <InputBox label='상세 주소' type='text' placeholder='상세 주소를 입력해주세요.' value={memberAddressDetail} setValue={setMemberAddressDetail} error={false} />
                        <input type='file' accept='.png, .jpg, .jpeg' onChange={profileImageChange} />
                    </>)}
                </div>
                <div className='auth-card-bottom'>
                    {page === 2 && (<>
                        <div className='auth-consent-box'>
                            <div className='auth-check-box' onClick={onConsentCheckHandler}>
                                {consent ? (<div className='check-round-fill-icon'></div>) : (<div className='check-ring-light-icon'></div>)}
                            </div>
                            <div className={consentError ? 'auth-consent-title-error' : 'auth-consent-title'}>{'개인정보동의'}</div>
                            <div className='auth-consent-link'>{'더보기>'}</div>
                        </div>
                        <div className='auth-button' onClick={onDetailsButtonClickHandler}>{'회원가입'}</div>
                    </>)}
                    <div className='auth-description-box'>
                        <div className='auth-description'>{'이미 계정이 있으신가요? '}<span className='description-emphasis' >{'로그인'}</span></div>
                    </div>
                </div>
            </div>
        );
    }

    //          render: 인증 페이지 렌더링         //
    return (
        <div id='auth-wrapper'>
            <div className='auth-container'>

                { view === 'sign-in' && <SignInCard /> }
                { view === 'sign-up' && <SignUpCard /> }
            </div>
        </div>
    );
}
