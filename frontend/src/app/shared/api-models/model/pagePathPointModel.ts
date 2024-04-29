/**
 * OpenAPI definition
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: v0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { PageableObject } from './pageableObject';
import { SortObject } from './sortObject';
import { PathPointModel } from './pathPointModel';


export interface PagePathPointModel { 
    totalElements?: number;
    totalPages?: number;
    pageable?: PageableObject;
    first?: boolean;
    last?: boolean;
    size?: number;
    content?: Array<PathPointModel>;
    number?: number;
    sort?: SortObject;
    numberOfElements?: number;
    empty?: boolean;
}
