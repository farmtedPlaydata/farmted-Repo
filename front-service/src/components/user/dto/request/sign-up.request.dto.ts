export default interface SignUpRequestDto {
    email: string;
    uuid: string | null;
    memberName: string;
    profileImage?: string | undefined | null;
    memberAddress: string;
    memberAddressDetail?: string;
    memberPhone: string;
    consent: boolean;
}