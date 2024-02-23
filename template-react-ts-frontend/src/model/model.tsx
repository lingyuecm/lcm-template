export type LcmWebResult<T> = {
    resultCode: number,
    resultMessage: string,
    resultBody: T
}

export type PagedList<T> = {
    totalCount: number,
    dataList: T[]
}
