export default interface SignUpRequestDto {
    email: string;
    uuid: string | null;
    memberName: string;
    memberAddress: string;
    memberAddressDetail?: string;
    memberPhone: string;
    consent: boolean;
}