import LoginUser from "../types/login-user.interface";
import {create} from "zustand";

interface UserStore {
    user: LoginUser | null;
    setUser: (user: LoginUser | null) => void;
}

const useUserStore = create<UserStore>(set => ({
    user: null,
    setUser: (user: LoginUser | null) => {set((state) => ({ ...state, user }))},
}));

export default useUserStore;