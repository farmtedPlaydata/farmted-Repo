import LoginUser from "../types/login-user.interface";
import {create} from "zustand";
import User from "../types/user.interface";

interface LoginUserStore {
    loginUser: User | null;
    setLoginUser: (loginUser: User) => void;
    resetLoginUser: () => void;
};

const userLoginUserStore = create<LoginUserStore>(set => ({
    loginUser: null,
    setLoginUser: loginUser => set(state => ({...state, loginUser})),
    resetLoginUser: () => set(state => ({...state, loginUser: null}))
}));

export default userLoginUserStore;