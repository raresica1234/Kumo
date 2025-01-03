export class EncoderUtil {
  public static encode(url: string): string {
    return url
      .split('/')
      .map((segment) => encodeURIComponent(segment))
      .join('/');
  }

  public static decode(url: string): string {
    return url
      .split('/')
      .map((segment) => decodeURIComponent(segment))
      .join('/');
  }
}
