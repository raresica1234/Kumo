export default interface ClientLocation {
    country: string;
    ipAddress: string;
    locationType: "ANDROID" | "IOS" | "WEB";
}