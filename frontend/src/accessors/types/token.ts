export default interface Token {
    jwtToken: string;
    refreshToken: string;
    validityMs: number;
}